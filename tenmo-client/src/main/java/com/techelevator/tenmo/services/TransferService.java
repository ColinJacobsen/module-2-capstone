package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class TransferService {

    // Properties
    private final String BASE_URL;
    private AuthenticatedUser currentUser;
    private String authToken;
    private final RestTemplate restTemplate = new RestTemplate();

    // Constructor
    public TransferService(String BASE_URL, AuthenticatedUser currentUser) {
        this.BASE_URL= BASE_URL;
        this.currentUser = currentUser;

    }

    // Getters and Setters
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

    // Service Methods
    public int makeTransfer(Transfer transfer) {
        HttpEntity<Transfer> entity = transferEntity(currentUser.getToken(), transfer);

        try {
            return restTemplate.postForObject(BASE_URL+"/transfers",
                    entity,
                    int.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return 0;
    }

    public void updateTransferStatus(Integer transferStatusId, int transferId){
        HttpEntity<Void> entity = transferVoidEntity(currentUser.getToken());

        try {
            restTemplate.put(BASE_URL + "/transfers/update/" + transferId + "/" + transferStatusId,
                    entity);
//            restTemplate.exchange(BASE_URL + "/transfers/update/" + transferId + "/" + transferStatusId,
//                    HttpMethod.PUT,
//                    entity,
//                    Void.class);
        }catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
    }

    public Transfer getTransferByTransferId(int transferId){
        Transfer transfer = null;
        HttpEntity<Void> entity = transferVoidEntity(currentUser.getToken());

        try{
            transfer =
                    restTemplate.exchange(BASE_URL + "/transers/" + transferId,
                            HttpMethod.GET,
                            entity,
                            Transfer.class).getBody();
        }catch (RestClientResponseException | ResourceAccessException e) {
        BasicLogger.log(e.getMessage());
    }
        return transfer;
    }


    public Transfer[] pendingRequest(int accountFrom){
        Transfer[] requests = null;
        HttpEntity<Void> entity = transferVoidEntity(currentUser.getToken());

        try{
            requests =
                    restTemplate.exchange(BASE_URL+"/transfers/pending/" + accountFrom,
                            HttpMethod.GET,
                            entity,
                            Transfer[].class).getBody();
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return requests;
    }

    // Wraps both the account debit and credit in a transaction method. Ensures both will complete or neither.
    public boolean doTransfer(Transfer transfer){  //CHANGED FROM BIGDECIMAL RETURN TO A BOOLEAN
        boolean transferCompleted = false;
        HttpEntity<Transfer> entity = transferEntity(authToken, transfer);

        try{
            restTemplate.put(BASE_URL+"/transfers/"+transfer.getTransferId(),
                    entity);

            transferCompleted = true;
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transferCompleted;
    }


    // Entity Convenience Methods

    private HttpEntity<Transfer> transferEntity (String token, Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(transfer, headers);
    }

    private HttpEntity<Void> transferVoidEntity (String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }



}
