package com.techelevator.controller;

import com.techelevator.tenmo.controller.AuthenticationController;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.RegisterUserDto;
import com.techelevator.tenmo.security.jwt.TokenProvider;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthenticationControllerTest {

    private AuthenticationController authenticationController;
    private TokenProvider tokenProvider;
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    private UserDao userDao;

    @Before
    public void setUp() {
        // Create the mock dependencies
        tokenProvider = mock(TokenProvider.class);
        userDao = mock(UserDao.class);

        // Create a real instance of AuthenticationManager
        authenticationManagerBuilder = mock(AuthenticationManagerBuilder.class);

        // Create the AuthenticationController with the dependencies
        authenticationController = new AuthenticationController(tokenProvider, authenticationManagerBuilder, userDao);
    }

    @Test
    public void testLogin() {
        // have not figured out this one yet
    }

    @Test
    public void testRegister() {
        // Create a mock register user DTO object
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setUsername("username");
        registerUserDto.setPassword("password");

        // Mock the userDao's create method to return true
        when(userDao.create(registerUserDto.getUsername(), registerUserDto.getPassword())).thenReturn(true);

        // Call the register method
        authenticationController.register(registerUserDto);

        // Verify that the userDao's create method was called with the correct parameters
        verify(userDao, times(1)).create(registerUserDto.getUsername(), registerUserDto.getPassword());
    }
}
