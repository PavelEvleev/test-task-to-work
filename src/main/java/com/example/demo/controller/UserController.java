package com.example.demo.controller;

import com.example.demo.entity.ResponseUserInfo;
import com.example.demo.entity.StatusCommand;
import com.example.demo.entity.User;
import com.example.demo.services.UserService;
import com.example.demo.services.exception.UserOperationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * User rest controller
 *
 * @author Pavel
 * @version 1
 */
@RestController
@RequestMapping("/v1/user")
public class UserController extends ApiErrorController {

    /**
     * Service for user data manipulation
     */
    private UserService service;

    /**
     * Constructor with injection
     *
     * @param service - service for data manipulation
     * @see UserService
     */
    public UserController(UserService service) {
        this.service = service;
    }


    /**
     * The Post method for creation user entity
     *
     * @param user - got data
     * @return uuid of the created user
     * @throws UserOperationException -  A custom exception to notify the client
     * @see UserOperationException
     */
    @PostMapping
    public ResponseEntity<ResponseUserInfo> create(@RequestBody User user) throws UserOperationException {
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseUserInfo(service.saveNewUser(user)));
    }

    /**
     * The Get method to retrieve user data
     *
     * @param uuid - unique identifier of user
     * @return user data
     * @throws UserOperationException -  A custom exception to notify the client
     * @see UserOperationException
     */
    @GetMapping
    public ResponseEntity<User> getUserByUUID(@RequestParam(value = "uuid") String uuid) throws UserOperationException {
        return ResponseEntity.ok(service.getUserByUUID(uuid));
    }

    /**
     * The Post method to change user status
     *
     * @param command - object with data to change user status
     * @return changed status
     * @throws UserOperationException -  A custom exception to notify the client
     * @see UserOperationException,StatusCommand
     */
    @PostMapping("/change-status")
    public ResponseEntity<StatusCommand> changeStatus(@RequestBody StatusCommand command) throws UserOperationException {
        return ResponseEntity.ok(service.changeStatus(command));
    }
}
