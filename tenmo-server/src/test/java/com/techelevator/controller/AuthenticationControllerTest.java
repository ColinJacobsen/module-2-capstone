package com.techelevator.controller;

import com.techelevator.tenmo.controller.AuthenticationController;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.LoginDto;
import com.techelevator.tenmo.model.LoginResponseDto;
import com.techelevator.tenmo.model.RegisterUserDto;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.security.jwt.TokenProvider;
import io.jsonwebtoken.lang.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthenticationController authenticationController;
    @MockBean
    private TokenProvider tokenProvider;
    @MockBean
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @MockBean
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
        User testUser = new User();
        testUser.setUsername("sassy");
        testUser.setPassword("sosassy");

        LoginDto testDto = new LoginDto();
        testDto.setUsername(testUser.getUsername());
        testDto.setPassword(testUser.getPassword());

        LoginResponseDto testResponseDto = new LoginResponseDto("test", testUser);

        when(authenticationController.login(testDto)).thenReturn(testResponseDto);

        Assert.notNull(authenticationController.login(testDto));
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
