package com.raghuvjoshi.customerrewardsservice.repository;

import com.raghuvjoshi.customerrewardsservice.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
