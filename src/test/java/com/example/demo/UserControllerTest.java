package com.example.demo;

import com.example.demo.controller.UserController;
import com.example.demo.entity.ResponseUserInfo;
import com.example.demo.entity.User;
import com.example.demo.services.UserService;
import com.example.demo.services.exception.UserOperationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Created by Pavel on 03.04.2018.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc rest;

    @MockBean
    private UserController controller;

    @Mock
    private UserService service;

    @Before
    public void init() throws UserOperationException {
        User expectedUser = new User("Pscascasha", "654654",
                "sacs5465", "me.as.asd@gmail.com", "cascsaasdsad");
        given(service.saveNewUser(expectedUser)).willReturn(new ResponseUserInfo(expectedUser.getUuid()).toString());
    }

    @Test
    public void shouldCreateUser() {
        String createUser = "{\"name\": \"Pscascasha\",\n" +
                "\t\"phone\": \"654654\",\n" +
                "\t\"password\": \"sacs5465\",\n" +
                "\t\"email\": \"me.as.asd@gmail.com\",\n" +
                "\t\"avatar\": \"cascsaasdsad\"}";

        try {
            final ResultActions resultActions = rest.perform(post("/v1/user").content(createUser).contentType(MediaType.APPLICATION_JSON_VALUE));
            HttpStatus status = HttpStatus.valueOf(resultActions.andReturn().getResponse().getStatus());
            assertThat(status).isEqualTo(HttpStatus.CREATED);
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }
}
