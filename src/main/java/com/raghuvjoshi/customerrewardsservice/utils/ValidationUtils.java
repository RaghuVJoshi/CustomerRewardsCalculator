package com.raghuvjoshi.customerrewardsservice.utils;

import java.time.Month;

public class ValidationUtils {

    /**
     * @param month - month from query parameters.
     * @return -  boolean flag validating input month.
     */
    public static boolean isValidMonth(String month) {
        try {
            Month.valueOf(month.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
