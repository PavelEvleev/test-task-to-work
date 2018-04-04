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


@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @InjectMocks
    UserService service;

    @Mock
    UserRepository repository;

    @Test
    public void shouldSaveUserIfAllFieldIsSet() throws UserOperationException {
        User expectedUser = new User("Pavel", "375447667131",
                "abracadabra1993", "sacsac@gmail.com", "some url");
        when(repository.saveAndFlush(expectedUser)).thenReturn(expectedUser);
        Long result = service.saveNewUser(expectedUser);

        assertThat(result).isEqualTo(expectedUser.getId());
    }

    @Test
    public void shouldNotSaveUserIfSomeFieldNotSet() {
        User expectedUser = new User();
        expectedUser.setName("Pasha");
        try {
            service.saveNewUser(expectedUser);
        } catch (UserOperationException e) {
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.FIELDS_CAN_NOT_BE_EMPTY);
        }
    }

    @Test
    public void shouldFindUserByUUIDIfExist() throws UserOperationException {
        User expectedUser = new User("Pavel", "375447667131",
                "abracadabra1993", "sacsac@gmail.com", "some url");
        when(repository.findById(expectedUser.getId())).thenReturn(Optional.of(expectedUser));
        Optional<User> optional = Optional.ofNullable(service.getUserByID(expectedUser.getId()));
        if (optional.isPresent()) {
            User result = optional.get();
            assertThat(result).isEqualTo(expectedUser);
        }
    }

    @Test
    public void shouldThrowExceptionIfUserNotExistByThatUUID() {

        Long someNotExistedUuid = 1L;

        when(repository.findById(someNotExistedUuid)).thenReturn(Optional.empty());
        try {
            Optional<User> optional = Optional.ofNullable(service.getUserByID(someNotExistedUuid));
            if (optional.isPresent()) {
                fail("It`s can be)");
            }
        } catch (UserOperationException e) {
            assertThat(e.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_EXISTED);
        }
    }

    @Test
    public void shouldChangeUserStatusByUUIDIfUserExist() throws UserOperationException {
        User expectedUser = new User("Pavel", "375447667131",
                "abracadabra1993", "sacsac@gmail.com", "some url");
        when(repository.findById(expectedUser.getId())).thenReturn(Optional.of(expectedUser));
        when(repository.saveAndFlush(expectedUser)).thenReturn(copy(expectedUser, true));

        StatusCommand command = new StatusCommand();
        command.setId(expectedUser.getId());
        command.setStatus(true);
        StatusCommand result = service.changeStatus(command);

        assertThat(result.getId()).isEqualTo(command.getId());
        assertThat(result.isStatus()).isTrue();
        assertThat(result.isLastStatus()).isFalse();
    }

    private User copy(User user, boolean status) {
        User result = new User();
        result.setOnline(status);
        result.setId(user.getId());
        result.setName(user.getName());
        result.setAvatar(user.getAvatar());
        result.setEmail(user.getEmail());
        result.setPassword(user.getPassword());
        result.setPhone(user.getPhone());
        return result;
    }
}
