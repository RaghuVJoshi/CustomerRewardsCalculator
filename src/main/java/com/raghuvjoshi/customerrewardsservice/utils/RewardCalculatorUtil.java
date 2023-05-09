package com.raghuvjoshi.customerrewardsservice.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * Utility functions to calculate application specific results.
 */
@Slf4j
public class RewardCalculatorUtil {
    public static final double FIRST_REWARD_THRESHOLD = 50D;
    public static final double SECOND_REWARD_THRESHOLD = 100D;

    /**
     *
     * @param transactionAmount - Transaction amount mapped from each transaction.
     * @return - calculated reward points .
     * For every dollar spent over $50 on the transaction, the customer receives one point.
     * In addition, for every dollar spent over $100, the customer receives another point.
     * Ex: for a $120 purchase, the customer receives
     * (120 - 50) x 1 + (120 - 100) x 1 = 90 points
     */
    public static final double computeRewardForTransaction(double transactionAmount) {
        boolean isAmountAboveSecondThreshold = transactionAmount >= SECOND_REWARD_THRESHOLD;
        if (isAmountAboveSecondThreshold) {
            return (transactionAmount - SECOND_REWARD_THRESHOLD) + (transactionAmount - FIRST_REWARD_THRESHOLD);
        } else {
            return Math.max(transactionAmount - FIRST_REWARD_THRESHOLD, 0D);
        }
    }
}
