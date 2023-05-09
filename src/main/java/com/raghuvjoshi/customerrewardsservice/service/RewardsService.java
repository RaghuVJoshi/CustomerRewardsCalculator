package com.raghuvjoshi.customerrewardsservice.service;

import com.raghuvjoshi.customerrewardsservice.exception.ServiceException;
import com.raghuvjoshi.customerrewardsservice.model.Transaction;

import java.util.List;
import java.util.Map;

public interface RewardsService {
   List<Transaction> getTransactions(Long customerId, String month) throws ServiceException;
   Map<String, Object> calculateRewards(List<Transaction> transactions);
}
