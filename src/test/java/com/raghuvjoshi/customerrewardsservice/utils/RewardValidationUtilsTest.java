package com.raghuvjoshi.customerrewardsservice.utils;
import com.raghuvjoshi.customerrewardsservice.exception.ServiceException;
import com.raghuvjoshi.customerrewardsservice.repository.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class RewardValidationUtilsTest {

    @InjectMocks
    private RewardValidationUtils rewardValidationUtils;

    @Mock
    private CustomerRepository customerRepository;

    @Test
    public void testIsValidMonthWithAllValidMonth() {
        assertTrue(rewardValidationUtils.isValidMonth("january"));
        assertTrue(rewardValidationUtils.isValidMonth("february"));
        assertTrue(rewardValidationUtils.isValidMonth("march"));
        assertTrue(rewardValidationUtils.isValidMonth("april"));
        assertTrue(rewardValidationUtils.isValidMonth("may"));
        assertTrue(rewardValidationUtils.isValidMonth("june"));
        assertTrue(rewardValidationUtils.isValidMonth("july"));
        assertTrue(rewardValidationUtils.isValidMonth("august"));
        assertTrue(rewardValidationUtils.isValidMonth("september"));
        assertTrue(rewardValidationUtils.isValidMonth("october"));
        assertTrue(rewardValidationUtils.isValidMonth("november"));
        assertTrue(rewardValidationUtils.isValidMonth("december"));
    }

    @Test
    public void testIsValidMonthWithInvalidMonth() {
        assertFalse(rewardValidationUtils.isValidMonth("jan"));
        assertFalse(rewardValidationUtils.isValidMonth("fEruary"));
        assertFalse(rewardValidationUtils.isValidMonth("marchh"));
        assertFalse(rewardValidationUtils.isValidMonth("apr"));
        assertFalse(rewardValidationUtils.isValidMonth("mayy"));
        assertFalse(rewardValidationUtils.isValidMonth("jun"));
        assertFalse(rewardValidationUtils.isValidMonth("julii"));
        assertFalse(rewardValidationUtils.isValidMonth("aug"));
        assertFalse(rewardValidationUtils.isValidMonth("septem"));
        assertFalse(rewardValidationUtils.isValidMonth("oct"));
        assertFalse(rewardValidationUtils.isValidMonth("novem"));
        assertFalse(rewardValidationUtils.isValidMonth("decem"));
        assertFalse(rewardValidationUtils.isValidMonth("notamonth"));
    }

    @Test
    public void testIsValidMonthWithMonthEqualsNull() {
        rewardValidationUtils.validateMonth(null);

        // No exception is thrown.
    }

    @Test
    public void testIsValidMonthWithValidMonth() {
        rewardValidationUtils.validateMonth("January");

        // No exception is thrown.
    }

    @Test
    public void testIsValidMonthWithInValidMonth() {
        String invalidMonth = "Janary";

        // Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            rewardValidationUtils.validateMonth(invalidMonth);
        });
        assertEquals("Invalid month: " + invalidMonth, exception.getErrorMessage());
        assertEquals(ServiceException.ExceptionType.INVALID_MONTH, exception.getExceptionType());
    }

    @Test
    public void testIsValidCustomerIdWithNullCustomerId() {
        rewardValidationUtils.validateCustomerIdInput(null);
    }

    @Test
    public void testIsValidCustomerIdWithValidCustomerId() {
        rewardValidationUtils.validateCustomerIdInput(1L);
    }

    @Test
    public void testIsValidCustomerIdWithInvalidCustomerId() {
        Long invalidCustomerId = -2L;

        // Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            rewardValidationUtils.validateCustomerIdInput(invalidCustomerId);
        });
        assertEquals("Invalid Customer Id: " + invalidCustomerId, exception.getErrorMessage());
        assertEquals(ServiceException.ExceptionType.INVALID_CUSTOMER_ID, exception.getExceptionType());
    }

    @Test
    public void testValidateCustomerIdInDatabase_WithValidCustomerId() {
        Long customerId = 1L;
        when(customerRepository.existsById(customerId)).thenReturn(true);

        rewardValidationUtils.validateCustomerIdInDatabase(customerId);

        verify(customerRepository, times(1)).existsById(customerId);
    }

    @Test
    public void testValidateCustomerIdInDatabase_WithNullCustomerId() {
        Long customerId = null;

        rewardValidationUtils.validateCustomerIdInDatabase(customerId);

        verify(customerRepository, never()).existsById(any());
    }

    @Test
    public void testValidateCustomerIdInDatabase_WithInvalidCustomerId() {
        Long customerId = 2L;
        when(customerRepository.existsById(customerId)).thenReturn(false);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            rewardValidationUtils.validateCustomerIdInDatabase(customerId);
        });

        assertEquals("Customer with ID " + customerId + " not found", exception.getErrorMessage());
        assertEquals(ServiceException.ExceptionType.CUSTOMER_NOT_FOUND, exception.getExceptionType());

        verify(customerRepository, times(1)).existsById(customerId);
    }

}

