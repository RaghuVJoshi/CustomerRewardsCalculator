package com.raghuvjoshi.customerrewardsservice.repository;

import com.raghuvjoshi.customerrewardsservice.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByCustomerIdAndTransactionDateBetween(Long customerId, LocalDate start, LocalDate end);

    List<Transaction> findByCustomerId(Long customerId);

    List<Transaction> findByTransactionDateBetween(LocalDate start, LocalDate end);
}
