package com.example.demo.controller;

import com.example.demo.services.exception.ApiError;
import com.example.demo.services.exception.UserOperationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to notify the client of errors
 *
 * @author Pavel
 * @version 1
 */
@RestController
public class ApiErrorController {

    /**
     * A method for sending notification of the client about specific error
     *
     * @param exception - specific exception
     * @return explanation of the error code
     * @see UserOperationException
     */
    @ExceptionHandler(UserOperationException.class)
    public ResponseEntity<ApiError> handleUserOperationException(UserOperationException exception) {
        return ResponseEntity.badRequest().body(new ApiError(exception.getMessage(), exception.getErrorCode().getCode()));
    }

}
