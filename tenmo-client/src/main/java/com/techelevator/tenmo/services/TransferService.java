package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import io.cucumber.java.ja.但し;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class TransferService {
    private final String BASE_URL;


    private AuthenticatedUser currentUser;
    private String authToken;
    private final RestTemplate restTemplate = new RestTemplate();

    public TransferService(String BASE_URL) {
        this.BASE_URL= BASE_URL;

    }

    public AuthenticatedUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(AuthenticatedUser currentUser) {
        this.currentUser = currentUser;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
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

    public BigDecimal doTransfer(Transfer transfer){
        try{
            restTemplate.put(BASE_URL+"/transfers/"+transfer.getTransferId(), transferEntity(currentUser.getToken(), transfer));
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return new BigDecimal(0);
    }

    public void updateTransferStatus(int transferStatusId, int transferId){
        try {
            restTemplate.put(BASE_URL + "/update/" + transferId, transferStatusId);
        }catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
    }

    public Transfer getTransferByTransferId(int transferId){
        Transfer transfer = null;
        try{
            transfer =
                    restTemplate.getForObject(BASE_URL +"/transfers/" + transferId, Transfer.class);
        }catch (RestClientResponseException | ResourceAccessException e) {
        BasicLogger.log(e.getMessage());
    }
        return transfer;
    }


    public Transfer[] pendingRequest(int accountFrom){
        Transfer[] requests = null;
        try{
            requests =
                    restTemplate.getForObject(BASE_URL+"/transfers/pending/" + accountFrom, Transfer[].class);
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return requests;
    }

    private HttpEntity<Transfer> transferEntity (String token, Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(transfer, headers);
    }



}
