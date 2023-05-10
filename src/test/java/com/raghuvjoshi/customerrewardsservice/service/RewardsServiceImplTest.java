package com.raghuvjoshi.customerrewardsservice.service;

import com.raghuvjoshi.customerrewardsservice.exception.ServiceException;
import com.raghuvjoshi.customerrewardsservice.model.Transaction;
import com.raghuvjoshi.customerrewardsservice.repository.TransactionRepository;
import com.raghuvjoshi.customerrewardsservice.model.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class RewardsServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private RewardsServiceImpl rewardsService;

    @Test
    public void testGetTransactionsForCustomerAndMonth() {
        Long customerId = 1L;
        String month = "January";

        LocalDate startOfMonth = LocalDate.of(LocalDate.now().getYear(), Month.valueOf(month.toUpperCase()), 1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        Customer c1 = new Customer(1L, "John");
        Customer c2 = new Customer(2L, "Jack");

        Transaction t1 = new Transaction(1L, 150D, startOfMonth.plusDays(1), c1);
        Transaction t2 = new Transaction(2L, 200D, endOfMonth.minusDays(1), c2);

        List<Transaction> transactions = Arrays.asList(t1, t2);

        when(transactionRepository.findByCustomerIdAndTransactionDateBetween(customerId, startOfMonth, endOfMonth))
                .thenReturn(transactions);

        List<Transaction> actual = rewardsService.getTransactions(customerId, month);

        assertEquals(transactions, actual);
    }

    @Test
    public void testGetTransactionsForCustomer() {
        Long customerId = 1L;

        Customer c1 = new Customer(1L, "John");
        Customer c2 = new Customer(2L, "Jack");

        Transaction t1 = new Transaction(1L, 150D, LocalDate.now(), c1);
        Transaction t2 = new Transaction(2L, 200D, LocalDate.now().minusDays(1), c2);

        List<Transaction> transactions = Arrays.asList(t1, t2);

        when(transactionRepository.findByCustomerId(customerId)).thenReturn(transactions);

        List<Transaction> actual = rewardsService.getTransactions(customerId, null);

        assertEquals(transactions, actual);
    }

    @Test
    public void testGetTransactionsForMonth() {
        String month = "February";
        Customer c1 = new Customer(1L, "John");
        Customer c2 = new Customer(2L, "Jack");

        LocalDate startOfMonth = LocalDate.of(LocalDate.now().getYear(), Month.valueOf(month.toUpperCase()), 1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        Transaction t1 = new Transaction(1L, 150D, startOfMonth.plusDays(1), c1);
        Transaction t2 = new Transaction(2L, 200D, endOfMonth.minusDays(1), c2);

        List<Transaction> transactions = Arrays.asList(t1, t2);

        when(transactionRepository.findByTransactionDateBetween(startOfMonth, endOfMonth))
                .thenReturn(transactions);

        List<Transaction> actual = rewardsService.getTransactions(null, month);

        assertEquals(transactions, actual);
    }

    @Test
    public void testGetAllTransactions() {
        Customer c1 = new Customer(1L, "John");
        Customer c2 = new Customer(2L, "Jack");

        Transaction t1 = new Transaction(1L, 100D, LocalDate.now(), c1);
        Transaction t2 = new Transaction(2L, 200D, LocalDate.now().minusDays(1), c2);

        List<Transaction> transactions = Arrays.asList(t1, t2);

        when(transactionRepository.findAll()).thenReturn(transactions);

        List<Transaction> actual = rewardsService.getTransactions(null, null);

        assertEquals(transactions, actual);
    }

    @Test
    public void testGetTransactionsReturnsAllTransactionsWhenCustomerIdAndMonthAreNull() throws ServiceException {
        // Arrange
        Customer c1 = new Customer(1L, "John");
        Customer c2 = new Customer(2L, "Jack");

        Transaction t1 = new Transaction(1L, 100D, LocalDate.now(), c1);
        Transaction t2 = new Transaction(2L, 200D, LocalDate.now().minusDays(1), c2);

        List<Transaction> transactions = Arrays.asList(t1, t2);
        when(transactionRepository.findAll()).thenReturn(transactions);

        // Act
        List<Transaction> actualTransactions = rewardsService.getTransactions(null, null);

        // Assert
        assertEquals(transactions, actualTransactions);
        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    public void testGetTransactionsReturnsTransactionsForGivenCustomerWhenMonthIsNull() throws ServiceException {
        // Arrange
        Customer c1 = new Customer(1L, "John");
        Customer c2 = new Customer(1L, "Jack");

        Transaction t1 = new Transaction(1L, 100D, LocalDate.now(), c1);
        Transaction t2 = new Transaction(2L, 200D, LocalDate.now().minusDays(1), c2);
        Transaction t3 = new Transaction(3L, 200D, LocalDate.now().minusDays(50), c1);

        List<Transaction> transactions = Arrays.asList(t1, t2, t3);
        List<Transaction> expectedTransactions = Arrays.asList(transactions.get(0), transactions.get(2));
        when(transactionRepository.findByCustomerId(1L)).thenReturn(expectedTransactions);

        // Act
        List<Transaction> actualTransactions = rewardsService.getTransactions(1L, null);

        // Assert
        assertEquals(expectedTransactions, actualTransactions);
        verify(transactionRepository, times(1)).findByCustomerId(1L);
    }

    @Test
    public void testGetTransactionsReturnsTransactionsForGivenMonthWhenCustomerIdIsNull() throws ServiceException {
        // Arrange
        String month = "January";

        Customer c1 = new Customer(1L, "John");
        Customer c2 = new Customer(1L, "Jack");

        Transaction t1 = new Transaction(1L, 100D, LocalDate.of(2023, Month.JANUARY, 1), c1);
        Transaction t2 = new Transaction(2L, 200D, LocalDate.of(2023, Month.JANUARY, 5), c2);
        Transaction t3 = new Transaction(3L, 200D, LocalDate.of(2023, Month.MARCH, 1), c1);

        List<Transaction> transactions = Arrays.asList(t1, t2, t3);
        List<Transaction> expectedTransactions = Arrays.asList(transactions.get(0), transactions.get(1));
        when(transactionRepository.findByTransactionDateBetween(any(LocalDate.class), any(LocalDate.class))).thenReturn(expectedTransactions);

        // Act
        List<Transaction> actualTransactions = rewardsService.getTransactions(null, month);

        // Assert
        assertEquals(expectedTransactions, actualTransactions);
        verify(transactionRepository, times(1)).findByTransactionDateBetween(any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    public void testGetTransactionsReturnsTransactionsForGivenCustomerAndMonth() throws ServiceException {
        // Arrange
        Long customerId = 1L;
        String month = "january";

        Customer c1 = new Customer(1L, "John");
        Customer c2 = new Customer(1L, "Jack");

        Transaction t1 = new Transaction(1L, 100D, LocalDate.of(2023, Month.JANUARY, 1), c1);
        Transaction t2 = new Transaction(2L, 200D, LocalDate.of(2023, Month.JANUARY, 5), c2);
        Transaction t3 = new Transaction(3L, 200D, LocalDate.of(2023, Month.MARCH, 1), c1);

        List<Transaction> transactions = Arrays.asList(t1, t2, t3);
        List<Transaction> expectedTransactions = Arrays.asList(transactions.get(0), transactions.get(1));

        LocalDate startOfMonth = LocalDate.of(LocalDate.now().getYear(), Month.valueOf(month.toUpperCase()), 1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        when(transactionRepository.findByCustomerIdAndTransactionDateBetween(customerId, startOfMonth, endOfMonth)).thenReturn(expectedTransactions);

        // Act
        List<Transaction> actualTransactions = rewardsService.getTransactions(customerId, month);

        // Assert
        assertEquals(expectedTransactions, actualTransactions);
        verify(transactionRepository, times(1)).findByCustomerIdAndTransactionDateBetween(customerId, startOfMonth, endOfMonth);
    }


    @Test
    public void testCalculateRewardsForEmptyTransactions() {
        Map<String, Object> rewards = rewardsService.calculateRewards(new ArrayList<>());
        assertNotNull(rewards);
        Map<Long, Map<String, Double>> rewardsPerCustomer = (Map<Long, Map<String, Double>>) rewards.get("rewardsPerCustomer");
        Map<String, Double> rewardsPerMonth = (Map<String, Double>) rewards.get("rewardsPerMonth");
        Map<Long, Double> totalRewardsPerCustomer = (Map<Long, Double>) rewards.get("totalRewardsPerCustomer");
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

        Map<String, Object> rewards = rewardsService.calculateRewards(transactions);
        assertNotNull(rewards);

        Map<Long, Map<String, Double>> rewardsPerCustomer = (Map<Long, Map<String, Double>>) rewards.get("rewardsPerCustomer");
        Map<String, Double> rewardsPerMonth = (Map<String, Double>) rewards.get("rewardsPerMonth");
        Map<Long, Double> totalRewardsPerCustomer = (Map<Long, Double>) rewards.get("totalRewardsPerCustomer");

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

