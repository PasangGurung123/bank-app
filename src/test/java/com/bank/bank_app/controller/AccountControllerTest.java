//package com.bank.bank_app.controller;
//
//import com.bank.bank_app.dto.AccountRequestDTO;
//import com.bank.bank_app.dto.AccountResponseDT0;
//import com.bank.bank_app.model.Account;
//import com.bank.bank_app.service.AccountService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.math.BigDecimal;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
///*
//Integration Testing
//
//@WebMvcTest(AccountController.class)
//Loads only the web layer (controllers, filters, etc.) but not full Spring context.
//
//@MockBean
//Creates a Mockito mock and registers it in Spring context.
//
//MockMvc
//Lets you simulate HTTP requests (GET, POST, PUT, DELETE) without running a server.
//
//ObjectMapper
//Converts Java objects to JSON (and vice versa) for request/response bodies.
//
//mockMvc.perform(...)
//Sends a mock HTTP request and lets you check the response status and JSON body.
//
//jsonPath
//Allows assertions on JSON response fields. Example:
//
//.andExpect(jsonPath("$.owner").value("John Doe"))
//
// */
//@WebMvcTest(AccountController.class) // loads only controller & related beans
//public class AccountControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private AccountService accountService;
//
//    @Autowired
//    private ObjectMapper objectMapper; // converts Java <-> JSON
//
//    private Account account;
//
//
//
//    @Test
//    void createTestAccount() throws Exception{
//
//        Account
//        when(accountService.createAccount(any(AccountRequestDTO.class))).thenReturn(account);
//    }
//}
