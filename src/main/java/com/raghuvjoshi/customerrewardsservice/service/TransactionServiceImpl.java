package com.raghuvjoshi.customerrewardsservice.service;

import com.raghuvjoshi.customerrewardsservice.exception.ServiceException;
import com.raghuvjoshi.customerrewardsservice.model.Transaction;
import com.raghuvjoshi.customerrewardsservice.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService{

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     *
     * @param customerId - customer Id from query parameters
     * @param month - month from query parameters
     * @return -  List of transactions filtered by customer Id and/or month
     * @throws ServiceException - Custom application specific exceptions.
     */
    @Override
    public List<Transaction> getTransactions(Long customerId, String month) throws ServiceException {
        LocalDate earliestMonth = null;
        LocalDate latestMonth = null;


        if (customerId != null && month != null) {
            // Get transactions for given customer and month
            LocalDate startOfMonth = LocalDate.of(LocalDate.now().getYear(), Month.valueOf(month.toUpperCase()), 1);
            LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());
            List<Transaction> transactions = transactionRepository.findByCustomerIdAndTransactionDateBetween(customerId, startOfMonth, endOfMonth);
            for (Transaction transaction : transactions) {
                LocalDate transactionDate = transaction.getTransactionDate();
                if (earliestMonth == null || transactionDate.isBefore(earliestMonth)) {
                    earliestMonth = transactionDate;
                }
                if (latestMonth == null || transactionDate.isAfter(latestMonth)) {
                    latestMonth = transactionDate;
                }
            }
            log.info("Retrieved transactions for Customer Id " + customerId + " and month from database" + month);
            return transactions;
        } else if (customerId != null) {
            // Get all transactions for given customer for each month.
            List<Transaction> transactions = transactionRepository.findByCustomerId(customerId);
            for (Transaction transaction : transactions) {
                LocalDate transactionDate = transaction.getTransactionDate();
                if (earliestMonth == null || transactionDate.isBefore(earliestMonth)) {
                    earliestMonth = transactionDate;
                }
                if (latestMonth == null || transactionDate.isAfter(latestMonth)) {
                    latestMonth = transactionDate;
                }
            }
            log.info("Retrieved transactions for Customer Id " + customerId + " for each month");
            return transactions;
        } else if (month != null) {
            // Get all transactions for given month for all customers.
            LocalDate startOfMonth = LocalDate.of(LocalDate.now().getYear(), Month.valueOf(month.toUpperCase()), 1);
            LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());
            List<Transaction> transactions = transactionRepository.findByTransactionDateBetween(startOfMonth, endOfMonth);
            for (Transaction transaction : transactions) {
                LocalDate transactionDate = transaction.getTransactionDate();
                if (earliestMonth == null || transactionDate.isBefore(earliestMonth)) {
                    earliestMonth = transactionDate;
                }
                if (latestMonth == null || transactionDate.isAfter(latestMonth)) {
                    latestMonth = transactionDate;
                }
            }
            log.info("Retrieved transactions for each Customer and month from database" + month);
            return transactions;
        } else {
            // Get all transactions
            List<Transaction> transactions = transactionRepository.findAll();
            for (Transaction transaction : transactions) {
                LocalDate transactionDate = transaction.getTransactionDate();
                if (earliestMonth == null || transactionDate.isBefore(earliestMonth)) {
                    earliestMonth = transactionDate;
                }
                if (latestMonth == null || transactionDate.isAfter(latestMonth)) {
                    latestMonth = transactionDate;
                }
            }
            log.info("Retrieved transactions for all customers for every month");
            return transactions;
        }
    }

}
