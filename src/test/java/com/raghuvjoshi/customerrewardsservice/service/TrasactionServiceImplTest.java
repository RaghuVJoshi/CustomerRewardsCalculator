package com.raghuvjoshi.customerrewardsservice.service;

import com.raghuvjoshi.customerrewardsservice.exception.ServiceException;
import com.raghuvjoshi.customerrewardsservice.model.Customer;
import com.raghuvjoshi.customerrewardsservice.model.Transaction;
import com.raghuvjoshi.customerrewardsservice.repository.TransactionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class TrasactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

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

        List<Transaction> actual = transactionService.getTransactions(customerId, month);

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

        List<Transaction> actual = transactionService.getTransactions(customerId, null);

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

        List<Transaction> actual = transactionService.getTransactions(null, month);

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

        List<Transaction> actual = transactionService.getTransactions(null, null);

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
        List<Transaction> actualTransactions = transactionService.getTransactions(null, null);

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
        List<Transaction> actualTransactions = transactionService.getTransactions(1L, null);

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
        List<Transaction> actualTransactions = transactionService.getTransactions(null, month);

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
        List<Transaction> actualTransactions = transactionService.getTransactions(customerId, month);

        // Assert
        assertEquals(expectedTransactions, actualTransactions);
        verify(transactionRepository, times(1)).findByCustomerIdAndTransactionDateBetween(customerId, startOfMonth, endOfMonth);
    }
}
