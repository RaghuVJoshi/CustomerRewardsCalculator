package com.raghuvjoshi.customerrewardsservice.utils;

import com.raghuvjoshi.customerrewardsservice.exception.ServiceException;
import com.raghuvjoshi.customerrewardsservice.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Month;

@Slf4j
@Component
public class RewardValidationUtils {

    private final CustomerRepository customerRepository;

    @Autowired
    public RewardValidationUtils(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * @param month - month from query parameters.
     * @return -  boolean flag validating input month.
     */
    public boolean isValidMonth(String month) {
        try {
            Month.valueOf(month.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public void validateCustomerIdInput(Long customerId) {
        if (customerId != null && customerId <= 0L) {
            log.error("Invalid Customer Id specified in request: " + customerId);
            throw new ServiceException("Invalid Customer Id: " + customerId, ServiceException.ExceptionType.INVALID_CUSTOMER_ID);
        }
    }

    public void validateCustomerIdInDatabase(Long customerId) {
        if (customerId != null && !customerRepository.existsById(customerId)) {
            log.error("Customer with Id " + customerId + "not found");
            throw new ServiceException("Customer with ID " + customerId + " not found", ServiceException.ExceptionType.CUSTOMER_NOT_FOUND);
        }
    }

    public void validateMonth(String month) {
        if (month != null && !isValidMonth(month)) {
            log.error("Invalid month specified in request: " + month);
            throw new ServiceException("Invalid month: " + month, ServiceException.ExceptionType.INVALID_MONTH);
        }
    }
}
