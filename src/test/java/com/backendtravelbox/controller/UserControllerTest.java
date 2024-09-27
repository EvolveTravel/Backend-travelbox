package com.backendtravelbox.controller;

import com.backendtravelbox.user.domain.model.aggregates.User;
import com.backendtravelbox.user.domain.model.commands.CreateUserCommand;
import com.backendtravelbox.user.domain.service.UserCommandService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserCommandService userCommandService;

    @Test
    void testCreateUser() throws Exception {

        CreateUserCommand createUserCommand = new CreateUserCommand(
                "Alessandro",
                "Vega",
                "alessandro123@email.com",
                "AlessandroVega",
                "password",
                "123456789"
        );

        String createUserJson = objectMapper.writeValueAsString(createUserCommand);

        when(userCommandService.handle(any(CreateUserCommand.class))).thenReturn(1L); // Simula la devolución de un ID de usuario

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/tripstore/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createUserJson));

        resultActions.andExpect(status().isOk());

        verify(userCommandService, times(1)).handle(any(CreateUserCommand.class));
    }






}