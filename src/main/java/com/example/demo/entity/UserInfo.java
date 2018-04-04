package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfo extends User {

    /**
     * Create a response object only for sending uuid to the created user
     * @param id - unique identifier of user
     */
    public UserInfo(Long id) {
        this.setId(id);
        this.setOnline(null);
    }


}
