package com.raghuvjoshi.customerrewardsservice.repository;

import com.raghuvjoshi.customerrewardsservice.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByCustomerIdAndTransactionDateBetween(Long customerId, LocalDate start, LocalDate end);

    List<Transaction> findByCustomerId(Long customerId);

    List<Transaction> findByTransactionDateBetween(LocalDate start, LocalDate end);
}
