package com.raghuvjoshi.customerrewardsservice.utils;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class RewardCalculatorUtilTest {

    @Test
    public void testComputeRewardForTransaction() {
        double transactionAmount = 100D;
        double expectedReward = 50D;

        double actualReward = RewardCalculatorUtil.computeRewardForTransaction(transactionAmount);

        assertEquals(expectedReward, actualReward, 0.0);
    }

    @Test
    public void testComputeRewardForTransactionWithNegativeAmount() {
        double transactionAmount = -50D;
        double expectedReward = 0D;

        double actualReward = RewardCalculatorUtil.computeRewardForTransaction(transactionAmount);

        assertEquals(expectedReward, actualReward, 0.0);
    }

    @Test
    public void testComputeRewardForTransactionWithAmountAboveSecondThreshold() {
        double transactionAmount = 150D;
        double expectedReward = 150D;

        double actualReward = RewardCalculatorUtil.computeRewardForTransaction(transactionAmount);

        assertEquals(expectedReward, actualReward, 0.0);
    }

    @Test
    public void testComputeRewardForTransactionWithAmountBetweenThresholds() {
        double transactionAmount = 75D;
        double expectedReward = 25D;

        double actualReward = RewardCalculatorUtil.computeRewardForTransaction(transactionAmount);

        assertEquals(expectedReward, actualReward, 0.0);
    }

    @Test
    public void testComputeRewardForTransactionWithAmountEqualToFirstThreshold() {
        double transactionAmount = 50D;
        double expectedReward = 0D;

        double actualReward = RewardCalculatorUtil.computeRewardForTransaction(transactionAmount);

        assertEquals(expectedReward, actualReward, 0.0);
    }

    @Test
    public void testComputeRewardForTransactionWithAmountEqualToSecondThreshold() {
        double transactionAmount = 10D;
        double expectedReward = 0D;

        double actualReward = RewardCalculatorUtil.computeRewardForTransaction(transactionAmount);

        assertEquals(expectedReward, actualReward, 0.0);
    }
}







