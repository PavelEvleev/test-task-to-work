package com.example.demo.services.exception;


/**
 * Custom exception to notify the client about the error
 * */

public class UserOperationException extends Exception {


    /**
     * Enum with explanation of error
     * @see ErrorCode
     * */
    private ErrorCode errorCode;

    public UserOperationException(ErrorCode errorCode){
        this.errorCode=errorCode;
    }

    @Override
    public String getMessage() {
        return this.errorCode.getDescription();
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
