package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.util.BasicLogger;
import io.cucumber.java.bs.A;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ActiveService {
    private final String BASE_URL;



    private AuthenticatedUser currentUser;
    private String authToken;
    private final RestTemplate restTemplate = new RestTemplate();


    public ActiveService(String BASE_URL, AuthenticatedUser currentUser){
        this.BASE_URL = BASE_URL;
        this.currentUser = currentUser;
    }
    public void setCurrentUser(AuthenticatedUser currentUser) {
        this.currentUser = currentUser;
    }

    public BigDecimal getUserBalance(AuthenticatedUser currentUser , int id) {
        BigDecimal balance = null;
        try {
            ResponseEntity<BigDecimal> response =
                    restTemplate.exchange(BASE_URL + "/user/"+id+"/balance", HttpMethod.GET, makeAuthEntity(currentUser.getToken()), BigDecimal.class);
            balance = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return balance;
    }

    public User[] getAllUsers(AuthenticatedUser currentUser){
        User[] users = null;
        try {
            ResponseEntity<User[]> response =
                    restTemplate.exchange(BASE_URL + "/user", HttpMethod.GET, makeAuthEntity(currentUser.getToken()), User[].class);
            users = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return users;
    }
    public User getUserByName(String name){
        for(User user : getAllUsers(currentUser)) {
            if(user.getUsername().equals(name)){
                return user;
            }
        }
        BasicLogger.log("Username not found");
        return null;
    }

    public int makeTransfer(Transfer transfer, AuthenticatedUser currentUser) {

        HttpEntity<Transfer> entity = transferEntity(currentUser.getToken(), transfer);
        try {
            return restTemplate.exchange(BASE_URL+"/transfers", HttpMethod.POST, entity, int.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
        BasicLogger.log(e.getMessage());
    }
        return 0;
    }

    public int userToAccount(User user){
        int accountId = 0;
        try{
            accountId = restTemplate.getForObject(BASE_URL + "/account/"+user.getId(), int.class);
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return accountId;
    }

    public BigDecimal doTransfer(Transfer transfer){
        try{
            restTemplate.put(BASE_URL+"/transfers/"+transfer.getTransferId(), transferEntity(currentUser.getToken(), transfer));
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return new BigDecimal(0);
    }


    private HttpEntity<Void> makeAuthEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }

    private HttpEntity<Transfer> transferEntity (String token, Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(transfer, headers);
    }
    private HttpEntity<User> userEntity (String token, User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return new HttpEntity<>(user, headers);
    }

}
