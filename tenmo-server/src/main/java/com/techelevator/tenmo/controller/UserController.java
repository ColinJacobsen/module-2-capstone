package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserDao userDao;

    public UserController(UserDao userDao){
        this.userDao = userDao;
    }

    @RequestMapping(value = "/{id}/balance", method = RequestMethod.GET)
    public BigDecimal getUserBalance(@PathVariable int id){
        return userDao.getUserBalance(id);

    }

    @GetMapping
    public List<User> getAllUsers(){
        return userDao.findAll();
    }

    @GetMapping (path = "/usernames")
    public List<String> getAllUsernames(){return userDao.findAllUsernames();}

    @GetMapping (path = "/search/{searchTerm}")
    List<String> searchUsernames(@PathVariable String searchTerm){
        return userDao.searchUsernames(searchTerm);
    }

//    @GetMapping(path = "/account/{id}/username")
//    public String getAccountUsername(@PathVariable int id){
//        return userDao.findUsernameByAccountId(id);
//    }
}
