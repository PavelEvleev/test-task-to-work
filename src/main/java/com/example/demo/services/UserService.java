package com.example.demo.services;

import com.example.demo.entity.StatusCommand;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.exception.ErrorCode;
import com.example.demo.services.exception.UserOperationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service for user data manipulation
 */
@Service
@Transactional
public class UserService {

    /**
     * Jpa repository for manipulation user data in db
     *
     * @see UserRepository
     */
    private UserRepository repository;


    public UserService(UserRepository repository) {
        this.repository = repository;
    }


    /**
     * Save new user in db
     *
     * @param user - incoming user data
     * @return unique identifier of new user
     * @throws UserOperationException - exception with explanation what go wrong
     * @see User
     */
    public Long saveNewUser(User user) throws UserOperationException {
        if (user.getName() == null || user.getAvatar() == null ||
                user.getEmail() == null || user.getPassword() == null || user.getPhone() == null) {
            throw new UserOperationException(ErrorCode.FIELDS_CAN_NOT_BE_EMPTY);
        }
        return repository.saveAndFlush(user).getId();
    }


    /**
     * Method for obtain user by unique identifier
     *
     * @param id - unique identifier of user
     * @return user data
     * @throws UserOperationException - exception with explanation what go wrong
     */
    public User getUserByID(Long id) throws UserOperationException {
        return repository.findById(id).orElseThrow(() -> new UserOperationException(ErrorCode.USER_NOT_EXISTED));
    }

    /**
     * Method change user status
     *
     * @param command - incoming data with unique identifier and new status
     * @return result of changing user status
     * @throws UserOperationException - exception with explanation what go wrong
     * @see StatusCommand
     */
    public StatusCommand changeStatus(StatusCommand command) throws UserOperationException {
        Optional<User> optional = repository.findById(command.getId());
        if (!optional.isPresent()) {
            throw new UserOperationException(ErrorCode.USER_NOT_EXISTED);
        }
        User user = optional.get();
        boolean lastStatus = user.isOnline();
        user.setOnline(command.isStatus());
        user = repository.saveAndFlush(user);
        StatusCommand result = new StatusCommand(user, lastStatus);
        return result;
    }
}
