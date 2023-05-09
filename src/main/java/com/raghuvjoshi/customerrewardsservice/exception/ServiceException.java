package com.raghuvjoshi.customerrewardsservice.exception;

/**
 * Custom exceptions to handle application specific error scenarios
 * INVALID_CUSTOMER_ID - Customer ID is specified but is in incorrect format
 * INVALID_MONTH - Month is specified but is in incorrect format
 * CUSTOMER_NOT_FOUND - Customer Id is valid but not present in transaction list.
 */
public class ServiceException extends RuntimeException{

        private final String message;
        private final ExceptionType type;

        public enum ExceptionType {INVALID_CUSTOMER_ID, INVALID_MONTH, CUSTOMER_NOT_FOUND
        }

        public ServiceException(String message, ExceptionType type) {
            this.message = message;
            this.type = type;
        }

        public String getErrorMessage() {
            return message;
        }

        public ExceptionType getExceptionType() {
            return type;
        }
    }
