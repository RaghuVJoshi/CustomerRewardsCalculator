package com.raghuvjoshi.customerrewardsservice.service;

import com.raghuvjoshi.customerrewardsservice.model.CustomerRewards;
import com.raghuvjoshi.customerrewardsservice.model.Transaction;
import com.raghuvjoshi.customerrewardsservice.utils.RewardCalculatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;

/**
 * Class to handle service logic related to retrieving transactions from table and computing reward points.
 */
@Service
@Slf4j
public class RewardsServiceImpl implements RewardsService{

    /**
     * Compute reward points for query response.
     *
     * @param transactions - List of transactions retrieved from database.
     * @return - Map of rewards objects per customer / month.
     */
    @Override
    public CustomerRewards calculateRewards(List<Transaction> transactions) {

        // Calculate rewards per customer and per month
        Map<Long, Map<String, Double>> rewardsPerCustomer = new HashMap<>();
        Map<String, Double> rewardsPerMonth = new HashMap<>();
        for (Transaction transaction : transactions) {
            long customerId = transaction.getCustomer().getId();
            LocalDate transactionDate = transaction.getTransactionDate();
            String transactionMonth = transactionDate.getMonth().getDisplayName(TextStyle.FULL, Locale.US);
            double amount = transaction.getAmount();
            log.debug("Computing reward points for transaction Id: " + transaction.getId());
            double points = RewardCalculatorUtil.computeRewardForTransaction(amount);

            // Update rewards per customer
            Map<String, Double> customerRewards = rewardsPerCustomer.computeIfAbsent(customerId, k -> new HashMap<>());
            double customerPoints = customerRewards.getOrDefault(transactionMonth, 0.0);
            customerRewards.put(transactionMonth, customerPoints + points);

            // Update rewards per transaction
            double monthPoints = rewardsPerMonth.getOrDefault(transactionDate.getMonth().getDisplayName(TextStyle.FULL, Locale.US), 0.0);
            rewardsPerMonth.put(transactionMonth, monthPoints + points);
        }

        // Calculate total rewards per customer
        Map<Long, Double> totalRewardsPerCustomer = new HashMap<>();
        Set<Map.Entry<Long, Map<String, Double>>> entries = rewardsPerCustomer.entrySet();
        for(Map.Entry<Long, Map<String, Double>> entry : entries) {
            double totalPoints = entry.getValue().values().stream().mapToDouble(Double::doubleValue).sum();
            long customerId = entry.getKey();
            totalRewardsPerCustomer.put(customerId, totalPoints);
        }

        // Add rewards to Customer rewards object and return them.
        return new CustomerRewards(rewardsPerCustomer, rewardsPerMonth, totalRewardsPerCustomer);
    }

}
