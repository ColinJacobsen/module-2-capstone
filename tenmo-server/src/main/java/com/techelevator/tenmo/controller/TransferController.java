package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for transactions
 */

@RestController
@RequestMapping("/transfers")
public class TransferController {

    private TransferDao transferDao;

    public TransferController(TransferDao transferDao) {this.transferDao = transferDao;}

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public int createTransfer(@RequestBody Transfer transfer){
        return transferDao.createTransfer(transfer);
    }

    @PutMapping(path = "/{id}")
    public void doTransfer(@PathVariable int id, @RequestBody Transfer transfer){
        transferDao.doTransfer(transfer, id);
    }

    @PutMapping(path = "/update/{id}")
    public void updateTransferStatus(int transferStatusId, @PathVariable int transferId){
        transferDao.updateTransferStatus(transferStatusId, transferId);
    }

    @GetMapping(path = "/history/{id}")
    public List<Transfer> transferHistory(@PathVariable int id){return transferDao.transferHistory(id);}

    @GetMapping(path = "/pending/{accountFrom}")
    public List<Transfer> pendingRequests(@PathVariable int accountFrom){
        return transferDao.pendingRequests(accountFrom);
    }
    @GetMapping(path = "/{id}")
    public Transfer getTransferById(@PathVariable int id){
        return transferDao.getTransferById(id);
    }

}
