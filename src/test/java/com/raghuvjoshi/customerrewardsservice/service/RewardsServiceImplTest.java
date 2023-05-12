package com.raghuvjoshi.customerrewardsservice.service;

import com.raghuvjoshi.customerrewardsservice.model.CustomerRewards;
import com.raghuvjoshi.customerrewardsservice.model.Transaction;
import com.raghuvjoshi.customerrewardsservice.model.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class RewardsServiceImplTest {

    @InjectMocks
    private RewardsServiceImpl rewardsService;

    @Test
    public void testCalculateRewardsForEmptyTransactions() {
        CustomerRewards rewards = rewardsService.calculateRewards(new ArrayList<>());
        assertNotNull(rewards);
        Map<Long, Map<String, Double>> rewardsPerCustomer = rewards.getRewardsPerCustomer();
        Map<String, Double> rewardsPerMonth = rewards.getRewardsPerMonth();
        Map<Long, Double> totalRewardsPerCustomer = rewards.getTotalRewardsPerCustomer();
        assertTrue(rewardsPerCustomer.isEmpty());
        assertTrue(rewardsPerMonth.isEmpty());
        assertTrue(totalRewardsPerCustomer.isEmpty());
    }
//
    @Test
    public void testCalculateRewardsForNonEmptyTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        Customer customer = new Customer(1L, "John Doe");
        transactions.add(new Transaction(1L, 50D, LocalDate.of(2022, 1, 10), customer));
        transactions.add(new Transaction(2L, 150D, LocalDate.of(2022, 2, 5), customer));
        transactions.add(new Transaction(3L, 200D, LocalDate.of(2022, 3, 15), customer));

        CustomerRewards rewards = rewardsService.calculateRewards(transactions);
        assertNotNull(rewards);

        Map<Long, Map<String, Double>> rewardsPerCustomer = rewards.getRewardsPerCustomer();
        Map<String, Double> rewardsPerMonth = rewards.getRewardsPerMonth();
        Map<Long, Double> totalRewardsPerCustomer = rewards.getTotalRewardsPerCustomer();

        assertEquals(1, rewardsPerCustomer.size());
        assertEquals(3, rewardsPerMonth.size());
        assertEquals(1, totalRewardsPerCustomer.size());

        assertTrue(rewardsPerCustomer.containsKey(1L));
        assertTrue(rewardsPerCustomer.get(1L).containsKey("January"));
        assertTrue(rewardsPerCustomer.get(1L).containsKey("February"));
        assertTrue(rewardsPerCustomer.get(1L).containsKey("March"));
        assertEquals(Double.valueOf(0), rewardsPerCustomer.get(1L).get("January"));
        assertEquals(Double.valueOf(150), rewardsPerCustomer.get(1L).get("February"));
        assertEquals(Double.valueOf(250), rewardsPerCustomer.get(1L).get("March"));

        assertTrue(rewardsPerMonth.containsKey("January"));
        assertTrue(rewardsPerMonth.containsKey("February"));
        assertTrue(rewardsPerMonth.containsKey("March"));
        assertEquals(Double.valueOf(0), rewardsPerMonth.get("January"));
        assertEquals(Double.valueOf(150), rewardsPerMonth.get("February"));
        assertEquals(Double.valueOf(250), rewardsPerMonth.get("March"));

        assertTrue(totalRewardsPerCustomer.containsKey(1L));
        assertEquals(Double.valueOf(400), totalRewardsPerCustomer.get(1L));
    }

}

