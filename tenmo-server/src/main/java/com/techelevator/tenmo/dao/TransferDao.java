package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public interface TransferDao {

    Transfer findTransferById(int transferId);
    void doTransfer(Transfer transfer, int id);
    int createTransfer(Transfer transfer);
    List<Transfer> transferHistory(int id);
}
