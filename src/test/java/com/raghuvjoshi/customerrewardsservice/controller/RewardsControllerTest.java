package com.raghuvjoshi.customerrewardsservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.raghuvjoshi.customerrewardsservice.exception.ServiceException;
import com.raghuvjoshi.customerrewardsservice.model.Transaction;
import com.raghuvjoshi.customerrewardsservice.model.Customer;
import com.raghuvjoshi.customerrewardsservice.repository.CustomerRepository;
import com.raghuvjoshi.customerrewardsservice.service.RewardsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class RewardsControllerTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private RewardsService rewardsService;

    @InjectMocks
    private RewardsController rewardsController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void testGetRewardsWithValidCustomerIdAndMonth() throws ServiceException {
        // Arrange
        Long customerId = 1L;
        String month = "January";
        List<Transaction> transactions = new ArrayList<>();
        Map<String, Object> rewards = new HashMap<>();
        when(customerRepository.existsById(customerId)).thenReturn(true);
        when(rewardsService.getTransactions(customerId, month)).thenReturn(transactions);
        when(rewardsService.calculateRewards(transactions)).thenReturn(rewards);

        // Act
        ResponseEntity<?> responseEntity = rewardsController.getRewards(customerId, month);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(rewards, responseEntity.getBody());
    }

    @Test
    public void testGetRewardsWithInvalidCustomerId() throws ServiceException {
        // Arrange
        Long customerId = 1L;
        String month = "january";
        when(customerRepository.existsById(customerId)).thenReturn(false);

        // Act
        ResponseEntity<?> responseEntity = rewardsController.getRewards(customerId, month);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(customerRepository, times(1)).existsById(customerId);
        verify(rewardsService, never()).getTransactions(any(), any());
        verify(rewardsService, never()).calculateRewards(any());
    }

    @Test
    public void testGetRewardsWithInvalidMonth() throws ServiceException {
        // Arrange
        Long customerId = 1L;
        String month = "invalid";
        when(customerRepository.existsById(customerId)).thenReturn(true);

        // Act
        ResponseEntity<?> responseEntity = rewardsController.getRewards(customerId, month);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verify(customerRepository, times(1)).existsById(customerId);
        verify(rewardsService, never()).getTransactions(any(), any());
        verify(rewardsService, never()).calculateRewards(any());
    }

        @Test
        public void testGetRewardsWithValidParams() throws Exception {
            // Arrange
            Long customerId = 1L;
            String month = "january";

            Customer c1 = new Customer(1L, "John");

            Transaction t1 = new Transaction(1L, 100D, LocalDate.of(2023, Month.JANUARY, 1), c1);
            Transaction t2 = new Transaction(2L, 200D, LocalDate.of(2023, Month.JANUARY, 5), c1);

            List<Transaction> transactions = Arrays.asList(t1, t2);
            Map<String, Object> expectedRewards = new HashMap<>();
            expectedRewards.put("customerId", customerId);
            expectedRewards.put("month", month);
            expectedRewards.put("rewards", 3);

            when(customerRepository.existsById(customerId)).thenReturn(true);
            when(rewardsService.getTransactions(customerId, month)).thenReturn(transactions);
            when(rewardsService.calculateRewards(transactions)).thenReturn(expectedRewards);

            // Act
            ResponseEntity<?> responseEntity = rewardsController.getRewards(customerId, month);

            // Assert
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(expectedRewards, responseEntity.getBody());
            verify(customerRepository, times(1)).existsById(customerId);
            verify(rewardsService, times(1)).getTransactions(customerId, month);
            verify(rewardsService, times(1)).calculateRewards(transactions);
        }

        @Test
        public void testGetRewardsWithMissingParams() throws Exception {

            // Act
            ResponseEntity<?> responseEntity = rewardsController.getRewards(null, null);

            // Assert
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            verify(customerRepository, never()).existsById(anyLong());
            verify(rewardsService, times(1)).getTransactions(null, null);
            verify(rewardsService, times(1)).calculateRewards(any());
        }

    @Test
    public void testGetRewardsWithInvalidMonthParameters() throws Exception {
        // Arrange
        Long customerId = 1L;
        String month = "invalid_month";

        // Act
        MvcResult result = mockMvc.perform(get("/api/rewards")
                        .param("customerId", String.valueOf(customerId))
                        .param("month", month))
                .andExpect(status().isBadRequest())
                .andReturn();

        // Assert
        String expectedErrorMessage = "Invalid month: invalid_month";
        String actualErrorMessage = result.getResponse().getContentAsString();
        assertTrue(actualErrorMessage.contains(expectedErrorMessage));
    }

    @Test
    public void testGetRewardsWithInvalidCustomerIdParameter() throws Exception {
        // Arrange
        Long customerId = -1L;
        String month = "January";

        // Act
        MvcResult result = mockMvc.perform(get("/api/rewards")
                        .param("customerId", String.valueOf(customerId))
                        .param("month", month))
                .andExpect(status().isBadRequest())
                .andReturn();

        // Assert
        String expectedErrorMessage = "Invalid Customer Id: -1";
        String actualErrorMessage = result.getResponse().getContentAsString();
        assertTrue(actualErrorMessage.contains(expectedErrorMessage));
    }

    @Test
    public void testGetRewardsWithLargeNumberOfTransactions() throws Exception {
        // Arrange
        Long customerId = 1L;
        String month = "January";

        Customer c1 = new Customer(1L, "John");
        List<Transaction> transactions = new ArrayList<>();
        for (int i = 1; i <= 10000; i++) {
            Transaction t = new Transaction((long) i, i * 100D, LocalDate.of(2023, Month.JANUARY, i % 30 + 1), c1);
            transactions.add(t);
        }
        when(rewardsService.getTransactions(customerId, month)).thenReturn(transactions);

        // Act
        MvcResult result = mockMvc.perform(get("/api/rewards")
                        .param("customerId", String.valueOf(customerId))
                        .param("month", month))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        Map<String, Object> rewards = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Map<String, Object>>(){});
        assertNotNull(rewards);
    }

}

