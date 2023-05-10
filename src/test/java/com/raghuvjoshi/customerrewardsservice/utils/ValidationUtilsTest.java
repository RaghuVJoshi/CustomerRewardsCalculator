package com.raghuvjoshi.customerrewardsservice.utils;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;

@SpringBootTest
public class ValidationUtilsTest {

    @Test
    public void testIsValidMonthWithValidMonth() {
        assertTrue(ValidationUtils.isValidMonth("january"));
        assertTrue(ValidationUtils.isValidMonth("february"));
        assertTrue(ValidationUtils.isValidMonth("march"));
        assertTrue(ValidationUtils.isValidMonth("april"));
        assertTrue(ValidationUtils.isValidMonth("may"));
        assertTrue(ValidationUtils.isValidMonth("june"));
        assertTrue(ValidationUtils.isValidMonth("july"));
        assertTrue(ValidationUtils.isValidMonth("august"));
        assertTrue(ValidationUtils.isValidMonth("september"));
        assertTrue(ValidationUtils.isValidMonth("october"));
        assertTrue(ValidationUtils.isValidMonth("november"));
        assertTrue(ValidationUtils.isValidMonth("december"));
    }

    @Test
    public void testIsValidMonthWithInvalidMonth() {
        assertFalse(ValidationUtils.isValidMonth("jan"));
        assertFalse(ValidationUtils.isValidMonth("fEruary"));
        assertFalse(ValidationUtils.isValidMonth("marchh"));
        assertFalse(ValidationUtils.isValidMonth("apr"));
        assertFalse(ValidationUtils.isValidMonth("mayy"));
        assertFalse(ValidationUtils.isValidMonth("jun"));
        assertFalse(ValidationUtils.isValidMonth("julii"));
        assertFalse(ValidationUtils.isValidMonth("aug"));
        assertFalse(ValidationUtils.isValidMonth("septem"));
        assertFalse(ValidationUtils.isValidMonth("oct"));
        assertFalse(ValidationUtils.isValidMonth("novem"));
        assertFalse(ValidationUtils.isValidMonth("decem"));
        assertFalse(ValidationUtils.isValidMonth("notamonth"));
    }

}

