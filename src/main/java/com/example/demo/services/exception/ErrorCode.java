package com.example.demo.services.exception;


/**
 * Enum with code and message of error
 * @author Pavel
 * */
public enum ErrorCode {
    USER_NOT_EXISTED(404, "User not existed with that ID"),
    FIELDS_CAN_NOT_BE_EMPTY(400, "All fields must be filled");

    private final int code;
    private final String description;

    private ErrorCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code + ": " + description;
    }
}
