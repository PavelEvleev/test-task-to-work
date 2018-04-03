package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseUserInfo extends User {

    /**
     * Create a response object only for sending uuid to the created user
     */
    public ResponseUserInfo(String uuid) {
        this.setUuid(uuid);
        this.setOnline(null);
    }


}
