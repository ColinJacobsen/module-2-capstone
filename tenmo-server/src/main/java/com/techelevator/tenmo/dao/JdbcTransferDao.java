package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transfer findTransferById(int transferId) {
        Transfer transfer = null;
        String sql = "SELECT * FROM transfer " +
                "WHERE transfer_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, Transfer.class, transferId);

        if (results.next()) {
            transfer = mapRowToTransfer(results);
        } return transfer;
    }

    @Override
    public void doTransfer(Transfer transfer, int id) {

        String sqlDoTransfer = "BEGIN; " +
                "UPDATE account SET balance = balance - ? " +
                "WHERE account_id = ?; " +
                "UPDATE account SET balance = balance + ? " +
                "WHERE account_id = ?; " +
                "COMMIT;";

        jdbcTemplate.update(sqlDoTransfer, transfer.getAmount(), transfer.getAccountFrom(), transfer.getAmount(), transfer.getAccountTo());

    }

    @Override
    public int createTransfer(Transfer transfer) {
        // update the balance where account from id = account id and account to id = account id
        String createTransfer = "INSERT INTO transfer (transfer_status_id, transfer_type_id, account_from, account_to, amount) " +
                "values(?,?,?,?,?) returning transfer_id";
        int transferStatus = transfer.getTransferStatus();
        int transferType = transfer.getTransferType();
        int senderId = transfer.getAccountFrom();
        int recipientId = transfer.getAccountTo();
        BigDecimal amount = transfer.getAmount();


        return jdbcTemplate.queryForObject(createTransfer, int.class, transferStatus, transferType, senderId, recipientId, amount);

    }

    @Override
    public List<Transfer> transferHistory(int id) {
        String sql = "select * from transfer where account_from = ? or account_to = ?";
        List<Transfer> listOfAccountTransfers = new ArrayList<>();
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id, id);
        while (rowSet.next()){
            Transfer transfer = mapRowToTransfer(rowSet);
            listOfAccountTransfers.add(transfer);
        }
        return listOfAccountTransfers;
    }


    private Transfer mapRowToTransfer(SqlRowSet rs) {

        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setTransferType(rs.getInt("transfer_type_id"));
        transfer.setTransferStatus(rs.getInt("transfer_status_id"));
        transfer.setAccountFrom(rs.getInt("account_from"));
        transfer.setAccountTo(rs.getInt("account_to"));
        transfer.setAmount(rs.getBigDecimal("amount"));

        return transfer;
    }
}
