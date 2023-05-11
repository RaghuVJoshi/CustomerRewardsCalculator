package com.raghuvjoshi.customerrewardsservice.service;

import com.raghuvjoshi.customerrewardsservice.exception.ServiceException;
import com.raghuvjoshi.customerrewardsservice.model.CustomerRewards;
import com.raghuvjoshi.customerrewardsservice.model.Transaction;

import java.util.List;

public interface RewardsService {
   List<Transaction> getTransactions(Long customerId, String month) throws ServiceException;
   CustomerRewards calculateRewards(List<Transaction> transactions);
}
