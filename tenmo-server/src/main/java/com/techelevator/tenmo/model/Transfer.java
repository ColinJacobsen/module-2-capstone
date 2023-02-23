package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private final String[] STATUS_OPTIONS = {"Pending", "Approved", "Rejected"};
    private final String[] TRANSFER_TYPES = {"Request", "Send"};
    private int transferId;
    private int transferStatus;
    private int transferType;
    private int accountFrom;
    private int accountTo;
    private BigDecimal amount;

    public Transfer(int transferStatus, int transferType, int accountFrom, int accountTo, BigDecimal amount) {
        // this.transferId = transferId;
        this.transferStatus = transferStatus;
        this.transferType = transferType;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getTransferStatus(){
        return this.transferStatus;
    }
    public int getTransferType() {
        return transferType;
    }

    public void setTransferStatus(int transferStatus) {
        this.transferStatus = transferStatus;
    }



    public void setTransferType(int transferType) {
        this.transferType = transferType;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "transferId=" + transferId +
                ", transferStatus='" + transferStatus + '\'' +
                ", transferType='" + transferType + '\'' +
                ", accountFrom=" + accountFrom +
                ", accountTo=" + accountTo +
                ", amount=" + amount +
                '}';
    }
}