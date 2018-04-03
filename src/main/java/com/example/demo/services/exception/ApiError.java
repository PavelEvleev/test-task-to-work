package com.example.demo.services.exception;

/**
 * Class for an explanation of the error
 *
 * @author Pavel
 * @version 1
 */
public class ApiError {

    /**
     * Field with message of error
     */
    private String message;

    /**
     * Field with code of error
     */
    private int errorCode;

    /**
     * Constructor
     *
     * @param message   - massage of error
     * @param errorCode - code of error
     */
    public ApiError(String message, int errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
