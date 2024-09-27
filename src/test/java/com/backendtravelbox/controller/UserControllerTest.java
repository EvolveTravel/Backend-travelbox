package com.backendtravelbox.controller;

import com.backendtravelbox.product.domain.model.commands.CreateProductCommand;
import com.backendtravelbox.trip.domain.model.commands.CreateTripCommand;
import com.backendtravelbox.trip.domain.service.TripCommandService;
import com.backendtravelbox.user.domain.model.aggregates.User;
import com.backendtravelbox.user.domain.model.commands.CreateUserCommand;
import com.backendtravelbox.user.domain.service.UserCommandService;
import com.backendtravelbox.product.domain.service.ProductCommandService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
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

    @MockBean
    private ProductCommandService productCommandService;

    @MockBean
    private TripCommandService tripCommandService;

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

        when(userCommandService.handle(any(CreateUserCommand.class))).thenReturn(1L);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createUserJson));

        resultActions.andExpect(status().isOk());

        verify(userCommandService, times(1)).handle(any(CreateUserCommand.class));

        int status = resultActions.andReturn().getResponse().getStatus();
        if (status == HttpStatus.OK.value()) {
            System.out.println("El usuario ha sido creado con éxito.");
        } else if (status == HttpStatus.BAD_REQUEST.value()) {
            System.out.println("Error: Solicitud incorrecta. Verifica los datos ingresados.");
        } else {
            System.out.println("Error inesperado. Código de estado: " + status);
        }

        Assertions.assertEquals(HttpStatus.OK.value(), status, "El resultado fue el esperado.");
    }

    @Test
    void testCreateProduct() throws Exception {

        CreateProductCommand createProductCommand = new CreateProductCommand(
                "Laptop",
                "Best Laptop in the world",
                19999.0,
                "",
                4.99,
                "Computer"
        );

        String createProductJson = objectMapper.writeValueAsString(createProductCommand);

        when(productCommandService.handle(any(CreateProductCommand.class))).thenReturn(1L);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createProductJson));

        resultActions.andExpect(status().isOk());

        verify(productCommandService, times(1)).handle(any(CreateProductCommand.class));

        int status = resultActions.andReturn().getResponse().getStatus();

        if (status == HttpStatus.OK.value()) {
            System.out.println("El producto ha sido creado con éxito.");
        } else if (status == HttpStatus.BAD_REQUEST.value()) {
            System.out.println("Error: Solicitud incorrecta. Verifica los datos ingresados.");
        } else {
            System.out.println("Error inesperado. Código de estado: " + status);
        }

        Assertions.assertEquals(HttpStatus.OK.value(), status, "El resultado fue el esperado.");
    }

    @Test
    void testCreateTrip() throws Exception {
        CreateTripCommand createTripCommand = new CreateTripCommand(
                "New York, United State",
                "Lima, Peru",
                "27/10/2024"
        );

        String createTripJson = objectMapper.writeValueAsString(createTripCommand);

        when(tripCommandService.handle(any(CreateTripCommand.class))).thenReturn(1L);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/trips")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createTripJson));

        resultActions.andExpect(status().isOk());

        verify(tripCommandService, times(1)).handle(any(CreateTripCommand.class));

        int status = resultActions.andReturn().getResponse().getStatus();

        if (status == HttpStatus.OK.value()) {
            System.out.println("El Viaje ha sido creado con éxito.");
        } else if (status == HttpStatus.BAD_REQUEST.value()) {
            System.out.println("Error: Solicitud incorrecta. Verifica los datos ingresados.");
        } else {
            System.out.println("Error inesperado. Código de estado: " + status);
        }

        Assertions.assertEquals(HttpStatus.OK.value(), status, "El resultado fue el esperado.");
    }

    @Test
    void testValidateNoLastName() throws Exception {
        // LastName vacío
        CreateUserCommand createUserCommand = new CreateUserCommand(
                "Alessandro",
                "" ,
                "alessandro123@email.com",
                "AlessandroVega",
                "password",
                "123456789"
        );

        String createUserJson = objectMapper.writeValueAsString(createUserCommand);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createUserJson));

        resultActions.andExpect(status().isBadRequest());

        resultActions.andExpect(result -> {
            String responseBody = result.getResponse().getContentAsString();
            assertTrue(responseBody.contains("Lastname must not be empty"), "Error message for missing phone number not found in response");
        });

        verify(userCommandService, times(0)).handle(any(CreateUserCommand.class));
    }

    @Test
    void testValidateNoPassword() throws Exception {
        // password vacío
        CreateUserCommand createUserCommand = new CreateUserCommand(
                "Alessandro",
                "Vega" ,
                "alessandro123@email.com",
                "AlessandroVega",
                "",
                "123456789"
        );

        String createUserJson = objectMapper.writeValueAsString(createUserCommand);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createUserJson));

        resultActions.andExpect(status().isBadRequest());

        resultActions.andExpect(result -> {
            String responseBody = result.getResponse().getContentAsString();
            assertTrue(responseBody.contains("Password must not be empty"), "Error message for missing password not found in response");
        });

        verify(userCommandService, times(0)).handle(any(CreateUserCommand.class));
    }

    @Test
    void testValidateNoPhoneNumber() throws Exception {
        // phoneNumber vacío
        CreateUserCommand createUserCommand = new CreateUserCommand(
                "Alessandro",
                "Vega" ,
                "alessandro123@email.com",
                "AlessandroVega",
                "password",
                ""
        );

        String createUserJson = objectMapper.writeValueAsString(createUserCommand);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createUserJson));

        resultActions.andExpect(status().isBadRequest());

        resultActions.andExpect(result -> {
            String responseBody = result.getResponse().getContentAsString();
            assertTrue(responseBody.contains("Phone Number must not be empty"), "Error message for missing Phone number not found in response");
        });

        verify(userCommandService, times(0)).handle(any(CreateUserCommand.class));
    }
}