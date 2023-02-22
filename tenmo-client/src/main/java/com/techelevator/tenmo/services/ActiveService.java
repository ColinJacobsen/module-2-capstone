package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

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
    private HttpEntity<Void> makeAuthEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }

}