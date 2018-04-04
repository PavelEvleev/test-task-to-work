package com.example.demo;

import com.example.demo.controller.UserController;
import com.example.demo.entity.StatusCommand;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.UserService;
import com.example.demo.services.exception.ErrorCode;
import com.example.demo.services.exception.UserOperationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.jayway.jsonpath.JsonPath.read;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class, secure = false)
public class UserControllerTest {

    @Autowired
    private MockMvc rest;

    @MockBean
    private UserService service;

    @MockBean
    private UserRepository repository;

    @Test
    public void shouldCreateUser() throws Exception {
        String createUser = "{\"name\": \"Pscascasha\",\n" +
                "\t\"phone\": \"654654\",\n" +
                "\t\"password\": \"sacs5465\",\n" +
                "\t\"email\": \"me.as.asd@gmail.com\",\n" +
                "\t\"avatar\": \"cascsaasdsad\"}";
        User expectedUser = new User("Pscascasha", "654654",
                "sacs5465", "me.as.asd@gmail.com", "cascsaasdsad");
        expectedUser.setId(1L);
        Long id = expectedUser.getId();
        Mockito.when(service.saveNewUser(Mockito.any(User.class))).thenReturn(id);
        final ResultActions resultActions = rest.perform(post("/v1/users").accept(MediaType.APPLICATION_JSON)
                .content(createUser).contentType(MediaType.APPLICATION_JSON));

        String result = resultActions.andReturn().getResponse().getContentAsString();
        String expect = "{id:" + id + "}";

        resultActions.andExpect(status().isCreated());
        JSONAssert.assertEquals(expect, result, true);
    }

    @Test
    public void shouldThrowExceptionIfSomeFieldNotSet() throws Exception {
        String createUser = "{\"name\":\"Pasha\"}";
        Mockito.when(service.saveNewUser(Mockito.any(User.class))).thenCallRealMethod();
        Mockito.when(repository.saveAndFlush(Mockito.any(User.class))).thenReturn(new User());

        final ResultActions resultActions = rest.perform(post("/v1/users").accept(MediaType.APPLICATION_JSON)
                .content(createUser).contentType(MediaType.APPLICATION_JSON));
        String result = resultActions.andReturn().getResponse().getContentAsString();
        String message = read(result, "$.message");
        Integer errorCode = read(result, "$.errorCode");

        resultActions.andExpect(status().isBadRequest());
        assertThat(message).isEqualTo(ErrorCode.FIELDS_CAN_NOT_BE_EMPTY.getDescription());
        assertThat(errorCode).isEqualTo(ErrorCode.FIELDS_CAN_NOT_BE_EMPTY.getCode());
    }

    @Test
    public void shouldReturnUserByUUIDIfExisted() throws Exception {
        User expectedUser = new User("Pscascasha", "654654",
                "sacs5465", "me.as.asd@gmail.com", "cascsaasdsad");
        expectedUser.setId(1L);
        Mockito.when(service.getUserByID(expectedUser.getId())).thenReturn(expectedUser);

        final ResultActions resultActions = rest.perform(get("/v1/users")
                .param("id", Long.toString(expectedUser.getId())).accept(MediaType.APPLICATION_JSON));
        String result = resultActions.andReturn().getResponse().getContentAsString();

        String name = read(result, "$.name");
        String phone = read(result, "$.phone");
        String email = read(result, "$.email");
        Integer id = read(result, "$.id");

        resultActions.andExpect(status().isOk());
        assertThat(name).isEqualTo(expectedUser.getName());
        assertThat(phone).isEqualTo(expectedUser.getPhone());
        assertThat(email).isEqualTo(expectedUser.getEmail());
        assertThat(id.longValue()).isEqualTo(expectedUser.getId());

    }

    @Test
    public void shouldThrowExceptionIfNotExisted() throws Exception {

        Mockito.when(service.getUserByID(Mockito.anyLong())).thenThrow(new UserOperationException(ErrorCode.USER_NOT_EXISTED));

        final ResultActions resultActions = rest.perform(get("/v1/users")
                .param("id", Long.toString(1L)).accept(MediaType.APPLICATION_JSON));
        String result = resultActions.andReturn().getResponse().getContentAsString();

        String message = read(result, "$.message");
        Integer errorCode = read(result, "$.errorCode");

        resultActions.andExpect(status().isBadRequest());
        assertThat(message).isEqualTo(ErrorCode.USER_NOT_EXISTED.getDescription());
        assertThat(errorCode).isEqualTo(ErrorCode.USER_NOT_EXISTED.getCode());

    }


    @Test
    public void shouldChangeUserStatusIfExisted() throws Exception {
        StatusCommand commandChange = new StatusCommand();
        commandChange.setId(1L);
        commandChange.setStatus(true);

        StatusCommand resultCommand = new StatusCommand();
        resultCommand.setId(1L);
        resultCommand.setStatus(true);
        resultCommand.setLastStatus(false);
        Mockito.when(service.changeStatus(commandChange)).thenReturn(resultCommand);

        final ResultActions resultActions = rest.perform(post("/v1/users/change-status")
                .content("{\n" +
                        "\t\"id\": \"" + Long.toString(1L) + "\",\n" +
                        "\t\"status\": true\n" +
                        "}").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON));
        String result = resultActions.andReturn().getResponse().getContentAsString();

        Integer id =  read(result, "$.id");
        boolean status = read(result, "$.status");
        boolean lastStatus = read(result, "$.lastStatus");

        resultActions.andExpect(status().isOk());
        assertThat(id.longValue()).isEqualTo(commandChange.getId());
        assertThat(status).isTrue();
        assertThat(lastStatus).isFalse();

    }

    @Test
    public void shouldThrowExceptionIfUserWithStatusCommandUUIDNotExisted() throws Exception {
        Mockito.when(service.changeStatus(Mockito.any(StatusCommand.class))).thenThrow(new UserOperationException(ErrorCode.USER_NOT_EXISTED));

        final ResultActions resultActions = rest.perform(post("/v1/users/change-status")
                .content("{\n" +
                        "\t\"uuid\": \"asacsscs\",\n" +
                        "\t\"status\": true\n" +
                        "}").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON));
        String result = resultActions.andReturn().getResponse().getContentAsString();

        String message = read(result, "$.message");
        Integer errorCode = read(result, "$.errorCode");

        resultActions.andExpect(status().isBadRequest());
        assertThat(message).isEqualTo(ErrorCode.USER_NOT_EXISTED.getDescription());
        assertThat(errorCode).isEqualTo(ErrorCode.USER_NOT_EXISTED.getCode());

    }

}
