package com.raghuvjoshi.customerrewardsservice.service;

import com.raghuvjoshi.customerrewardsservice.exception.ServiceException;
import com.raghuvjoshi.customerrewardsservice.model.Transaction;

import java.util.List;

public interface TransactionService {
    List<Transaction> getTransactions(Long customerId, String month) throws ServiceException;
}
