package com.raghuvjoshi.customerrewardsservice.model;

import java.util.Map;

public class CustomerRewards {
    private Map<Long, Map<String, Double>> rewardsPerCustomer;
    private Map<String, Double> rewardsPerMonth;
    private Map<Long, Double> totalRewardsPerCustomer;

    public CustomerRewards() {

    }

    public CustomerRewards(Map<Long, Map<String, Double>> rewardsPerCustomer, Map<String, Double> rewardsPerMonth, Map<Long, Double> totalRewardsPerCustomer) {
        this.rewardsPerCustomer = rewardsPerCustomer;
        this.rewardsPerMonth = rewardsPerMonth;
        this.totalRewardsPerCustomer = totalRewardsPerCustomer;
    }

    public Map<Long, Map<String, Double>> getRewardsPerCustomer() {
        return rewardsPerCustomer;
    }

    public Map<String, Double> getRewardsPerMonth() {
        return rewardsPerMonth;
    }

    public Map<Long, Double> getTotalRewardsPerCustomer() {
        return totalRewardsPerCustomer;
    }

    public void setRewardsPerCustomer(Map<Long, Map<String, Double>> rewardsPerCustomer) {
        this.rewardsPerCustomer = rewardsPerCustomer;
    }

    public void setRewardsPerMonth(Map<String, Double> rewardsPerMonth) {
        this.rewardsPerMonth = rewardsPerMonth;
    }

    public void setTotalRewardsPerCustomer(Map<Long, Double> totalRewardsPerCustomer) {
        this.totalRewardsPerCustomer = totalRewardsPerCustomer;
    }
}
