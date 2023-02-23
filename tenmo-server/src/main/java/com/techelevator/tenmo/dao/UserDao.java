package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface UserDao {

    List<User> findAll();

    User getUserById(int id);

    User findByUsername(String username);

    int findIdByUsername(String username);

    boolean create(String username, String password);

    BigDecimal getUserBalance(int id);

    void doTransfer(int senderId, int recipientId, BigDecimal amount);

    void createTransfer(Transfer transfer);

    }
