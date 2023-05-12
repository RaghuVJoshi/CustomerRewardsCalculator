package com.raghuvjoshi.customerrewardsservice.service;

import com.raghuvjoshi.customerrewardsservice.model.CustomerRewards;
import com.raghuvjoshi.customerrewardsservice.model.Transaction;

import java.util.List;

public interface RewardsService {

   CustomerRewards calculateRewards(List<Transaction> transactions);
}
