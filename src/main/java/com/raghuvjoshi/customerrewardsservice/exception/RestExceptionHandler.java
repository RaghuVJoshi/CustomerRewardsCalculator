package com.raghuvjoshi.customerrewardsservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Custom exception handler to convert Application specific exception into Response Entity objects.
 */
@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Object> handleServiceException(ServiceException ex) {
        HttpStatus status = null;
        ServiceException.ExceptionType exceptionType = ex.getExceptionType();
        log.debug("Raised exception of type " + exceptionType);
        if(exceptionType == ServiceException.ExceptionType.CUSTOMER_NOT_FOUND) {
            status = HttpStatus.NOT_FOUND;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }

        ErrorDto errorDto = new ErrorDto(ex.getErrorMessage(), exceptionType);
        return ResponseEntity.status(status).body(errorDto);
    }

    private static class ErrorDto {
        private final String errorMessage;
        private final ServiceException.ExceptionType exceptionType;

        public ErrorDto(String errorMessage, ServiceException.ExceptionType exceptionType) {
            this.errorMessage = errorMessage;
            this.exceptionType = exceptionType;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public ServiceException.ExceptionType getExceptionType() {
            return exceptionType;
        }
    }

}

