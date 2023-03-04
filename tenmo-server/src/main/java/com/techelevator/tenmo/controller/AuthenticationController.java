package com.techelevator.tenmo.controller;

import javax.validation.Valid;

import com.techelevator.tenmo.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.security.jwt.TokenProvider;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controller to authenticate users.
 */
@RestController
public class AuthenticationController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserDao userDao;

    public AuthenticationController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, UserDao userDao) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userDao = userDao;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public LoginResponseDto login(@Valid @RequestBody LoginDto loginDto) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication, false);
        
        User user = userDao.findByUsername(loginDto.getUsername());

        return new LoginResponseDto(jwt, user);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public void register(@Valid @RequestBody RegisterUserDto newUser) {
        if (!userDao.create(newUser.getUsername(), newUser.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User registration failed.");
        }
    }

    @GetMapping(path = "/account/{id}")
    public int userToAccount(@PathVariable int id){
        return userDao.userToAccount(id);
    }

    @GetMapping(path = "/account/{id}/balance")
    public BigDecimal getAccountBalance(@PathVariable int id){
        return userDao.getBalanceByAccountId(id);
    }

    @GetMapping(path = "/account/{id}/username")
    public String getAccountUsername(@PathVariable int id){
        return userDao.findUsernameByAccountId(id);
    }

    // Moved into other controllers

//    @RequestMapping(value = "/user/{id}/balance", method = RequestMethod.GET)
//    public BigDecimal getUserBalance(@PathVariable int id){
//        return userDao.getUserBalance(id);
//
//    }

//    @GetMapping(path = "/user")
//    public List<User> getAllUsers(){
//        return userDao.findAll();
//    }


//    @PostMapping(path = "/transfers")
//    @ResponseStatus(HttpStatus.CREATED)
//    public int createTransfer(@RequestBody Transfer transfer){
//        return userDao.createTransfer(transfer);
//    }

//    @PutMapping(path = "/transfers/{id}")
//    public void doTransfer(@PathVariable int id, @RequestBody Transfer transfer){
//        userDao.doTransfer(transfer, id);
//    }

//    @GetMapping(path = "/history/{id}")
//    public List<Transfer> transferHistory(@PathVariable int id){return userDao.transferHistory(id);}


//    @GetMapping(path = "/transfers/{transferId}")
//    public Transfer getTransferById(@PathVariable int transferId){
//        return userDao.findTransferById(transferId);
//    }




}

