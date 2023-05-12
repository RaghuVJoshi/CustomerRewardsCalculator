package com.raghuvjoshi.customerrewardsservice.integration;

import com.raghuvjoshi.customerrewardsservice.model.Customer;
import com.raghuvjoshi.customerrewardsservice.model.CustomerRewards;
import com.raghuvjoshi.customerrewardsservice.repository.CustomerRepository;
import com.raghuvjoshi.customerrewardsservice.repository.TransactionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RewardsCalculatorIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Test
    public void testGetRewardsForCustomer() {
        // Assume a customer from existing database
        Customer customer = new Customer(1L, "John");

        // Get rewards for the customer
        ResponseEntity<CustomerRewards> response = restTemplate.exchange(
                "/api/rewards?customerId={customerId}",
                HttpMethod.GET,
                null,
                CustomerRewards.class,
                customer.getId()
        );

        // Verify the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        CustomerRewards rewards = response.getBody();
        assertNotNull(rewards);
        assertEquals(customer.getId(), rewards.getRewardsPerCustomer().keySet().iterator().next());
        assertEquals(200, rewards.getTotalRewardsPerCustomer().get(customer.getId()).intValue());
        assertEquals(3, rewards.getRewardsPerMonth().size());
    }

    @Test
    public void testGetRewardsForAllCustomers() {

        //Assume customers from existing database
        Customer customer1 = new Customer(1L, "John");
        Customer customer2 = new Customer(2L, "Jane");

        // Get rewards for all customers
        ResponseEntity<CustomerRewards> response = restTemplate.exchange(
                "/api/rewards",
                HttpMethod.GET,
                null,
                CustomerRewards.class
        );

        // Verify the response is not empty
        assertEquals(HttpStatus.OK, response.getStatusCode());
        CustomerRewards rewards = response.getBody();
        assertNotNull(rewards);
        assertEquals(4, rewards.getTotalRewardsPerCustomer().size());

        Map<String, Double> rewardsForCustomer1 = rewards.getRewardsPerCustomer().get(customer1.getId());
        assertNotNull(rewardsForCustomer1);
        Map<String, Double> rewardsForCustomer2 = rewards.getRewardsPerCustomer().get(customer2.getId());
        assertNotNull(rewardsForCustomer2);

        }
    }
