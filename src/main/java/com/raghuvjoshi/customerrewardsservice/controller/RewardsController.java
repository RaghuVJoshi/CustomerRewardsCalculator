package com.raghuvjoshi.customerrewardsservice.controller;

import com.raghuvjoshi.customerrewardsservice.exception.ServiceException;
import com.raghuvjoshi.customerrewardsservice.model.CustomerRewards;
import com.raghuvjoshi.customerrewardsservice.model.Transaction;
import com.raghuvjoshi.customerrewardsservice.utils.ValidationUtils;
import com.raghuvjoshi.customerrewardsservice.exception.RestExceptionHandler;
import com.raghuvjoshi.customerrewardsservice.repository.CustomerRepository;
import com.raghuvjoshi.customerrewardsservice.service.RewardsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Controller class to handle data requests to Customer Rewards Service application.
 */
@RestController
@RequestMapping("/api/rewards")
@Slf4j
public class RewardsController {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RewardsService rewardsService;

    /**
     * Iterates through transactions filtered by API query parameters and computes reward points.
     * @param customerId - customer for whom reward points must be returned. If not specified, retrieve data for all customers
     * @param month - months for which reward points must be returned. If not specified, retrieve data for every month transactions are present
     * @return - Response containing JSON representation of rewards calculated. If an exception is generated, return suitable error message via handler.
     */
    @GetMapping
    public ResponseEntity<?> getRewards(@RequestParam(required = false) Long customerId,
                                        @RequestParam(required = false) String month){


        RestExceptionHandler handler = new RestExceptionHandler();;
        try {

            // Validate query parameters
            if (customerId != null && customerId <= 0L) {
                log.error("Invalid Customer Id specified in request: " + customerId);
                throw new ServiceException("Invalid Customer Id: " + customerId, ServiceException.ExceptionType.INVALID_CUSTOMER_ID);
            }
            if (customerId != null && !customerRepository.existsById(customerId)) {
                log.error("Customer with Id " + customerId + "not found");
                throw new ServiceException("Customer with ID " + customerId + " not found", ServiceException.ExceptionType.CUSTOMER_NOT_FOUND);
            }
            if (month != null && !ValidationUtils.isValidMonth(month)) {
                log.error("Invalid month specified in request: " + month);
                throw new ServiceException("Invalid month: " + month, ServiceException.ExceptionType.INVALID_MONTH);
            }
            // Get transactions for given query parameters
            List<Transaction> transactions = rewardsService.getTransactions(customerId, month);

            // Calculate rewards for queried transactions
            CustomerRewards rewards = rewardsService.calculateRewards(transactions);

            return ResponseEntity.ok(rewards);
        } catch (ServiceException ex) {
            log.error("Found exception of type " + ex.getExceptionType());
            return handler.handleServiceException(ex);
        }

    }
}
