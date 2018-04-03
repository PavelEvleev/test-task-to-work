package com.example.demo;

import com.example.demo.entity.StatusCommand;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.UserService;
import com.example.demo.services.exception.ErrorCode;
import com.example.demo.services.exception.UserOperationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.when;

/**
 * Created by Pavel on 03.04.2018.
 */

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @InjectMocks
    UserService service;

    @Mock
    UserRepository repository;

    @Test
    public void shouldSaveUserIfAllFieldIsSet() {
        User expectedUser = new User("Pavel", "375447667131",
                "abracadabra1993", "sacsac@gmail.com", "some url");
        when(repository.saveAndFlush(expectedUser)).thenReturn(expectedUser);
        String result = "";
        try {
            result = service.saveNewUser(expectedUser);

        } catch (UserOperationException e) {
            fail(e.getMessage());
        }
        assertThat(result).isEqualTo(expectedUser.getUuid());
    }

    @Test
    public void shouldNotSaveUserIfSomeFieldNotSet() {
        User expectedUser = new User();
        expectedUser.setName("Pasha");
        try {
            String result = service.saveNewUser(expectedUser);
        } catch (UserOperationException e) {
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.FIELDS_CAN_NOT_BE_EMPTY);
        }
    }

    @Test
    public void shouldFindUserByUUIDIfExist() {
        User expectedUser = new User("Pavel", "375447667131",
                "abracadabra1993", "sacsac@gmail.com", "some url");
        when(repository.findByUuid(expectedUser.getUuid())).thenReturn(Optional.of(expectedUser));
        try {
            Optional<User> optional = Optional.ofNullable(service.getUserByUUID(expectedUser.getUuid()));
            if (optional.isPresent()) {
                User result = optional.get();
                assertThat(result).isEqualTo(expectedUser);
            }
        } catch (UserOperationException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void shouldThrowExceptionIfUserNotExistByThatUUID() {

        String someNotExistedUuid = "asas-asd12-1221d-as-2332fsc";

        when(repository.findByUuid(someNotExistedUuid)).thenReturn(Optional.empty());
        try {
            Optional<User> optional = Optional.ofNullable(service.getUserByUUID(someNotExistedUuid));
            if (optional.isPresent()) {
                fail("It`s can be)");
            }
        } catch (UserOperationException e) {
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_EXISTED);
        }
    }

    @Test
    public void shouldChangeUserStatusByUUIDIfUserExist() {
        User expectedUser = new User("Pavel", "375447667131",
                "abracadabra1993", "sacsac@gmail.com", "some url");
        when(repository.findByUuid(expectedUser.getUuid())).thenReturn(Optional.of(expectedUser));
        when(repository.saveAndFlush(expectedUser)).thenReturn(copy(expectedUser, true));

        StatusCommand command = new StatusCommand();
        command.setUuid(expectedUser.getUuid());
        command.setStatus(true);
        try {
            StatusCommand result = service.changeStatus(command);

            assertThat(result.getUuid()).isEqualTo(command.getUuid());
            assertThat(result.isStatus()).isTrue();
            assertThat(result.isLastStatus()).isFalse();
        } catch (UserOperationException e) {
            fail(e.getMessage());
        }
    }

    private User copy(User user, boolean status) {
        User result = new User();
        result.setOnline(status);
        result.setUuid(user.getUuid());
        result.setName(user.getName());
        result.setAvatar(user.getAvatar());
        result.setEmail(user.getEmail());
        result.setPassword(user.getPassword());
        result.setPhone(user.getPhone());
        return result;
    }
}
