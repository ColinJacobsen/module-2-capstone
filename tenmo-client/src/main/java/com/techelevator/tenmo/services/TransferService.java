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
    private final String BASE_URL;


    private AuthenticatedUser currentUser;
    private String authToken;
    private final RestTemplate restTemplate = new RestTemplate();

    public TransferService(String BASE_URL, AuthenticatedUser currentUser) {
        this.BASE_URL= BASE_URL;
        this.currentUser = currentUser;

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

    public int makeTransfer(Transfer transfer) {

        HttpEntity<Transfer> entity = transferEntity(currentUser.getToken(), transfer);
        try {
            return restTemplate.exchange(BASE_URL+"/transfers", HttpMethod.POST, entity, int.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return 0;
    }



    public boolean doTransfer(Transfer transfer){  //CHANGED FROM BIGDECIMAL RETURN TO A BOOLEAN
        boolean transferCompleted = false;

        try{
            restTemplate.put(BASE_URL+"/transfers/"+transfer.getTransferId(), transferEntity(authToken, transfer));
            transferCompleted = true;
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transferCompleted;
    }

    public void updateTransferStatus(Integer transferStatusId, int transferId){

        HttpEntity<Object> entity = transferVoidEntity(currentUser.getToken());

        try {
            restTemplate.put(BASE_URL + "/transfers/update/" + transferId + "/" + transferStatusId, entity);
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

    private HttpEntity<Object> transferVoidEntity (String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(Void.class, headers);
    }



}
