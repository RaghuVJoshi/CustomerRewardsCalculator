package com.raghuvjoshi.customerrewardsservice.controller;

import com.raghuvjoshi.customerrewardsservice.model.CustomerRewards;
import com.raghuvjoshi.customerrewardsservice.model.Transaction;
import com.raghuvjoshi.customerrewardsservice.service.TransactionService;
import com.raghuvjoshi.customerrewardsservice.utils.RewardValidationUtils;
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

    private final RewardValidationUtils rewardValidationUtils;
    private final RewardsService rewardsService;
    private final TransactionService transactionService;

    @Autowired
    public RewardsController(RewardValidationUtils rewardValidationUtils,
                             RewardsService rewardsService,
                             TransactionService transactionService) {
        this.rewardValidationUtils = rewardValidationUtils;
        this.rewardsService = rewardsService;
        this.transactionService = transactionService;
    }

    /**
     * Iterates through transactions filtered by API query parameters and computes reward points.
     * @param customerId - customer for whom reward points must be returned. If not specified, retrieve data for all customers
     * @param month - months for which reward points must be returned. If not specified, retrieve data for every month transactions are present
     * @return - Response containing JSON representation of rewards calculated. If an exception is generated, return suitable error message via handler.
     */
    @GetMapping
    public ResponseEntity<?> getRewards(@RequestParam(required = false) Long customerId,
                                        @RequestParam(required = false) String month){

        rewardValidationUtils.validateCustomerIdInput(customerId);
        rewardValidationUtils.validateCustomerIdInDatabase(customerId);
        rewardValidationUtils.validateMonth(month);

        // Get transactions for given query parameters
        List<Transaction> transactions = transactionService.getTransactions(customerId, month);

        // Calculate rewards for queried transactions
        CustomerRewards rewards = rewardsService.calculateRewards(transactions);

        return ResponseEntity.ok(rewards);
    }
}
