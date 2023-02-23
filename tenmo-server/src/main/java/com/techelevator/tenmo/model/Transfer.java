package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private final String[] STATUS_OPTIONS = {"Pending", "Approved", "Rejected"};
    private final String[] TRANSFER_TYPES = {"Request", "Send"};
    private int transferId;
    private String transferStatus;
    private String transferType;
    private int accountFrom;
    private int accountTo;
    private BigDecimal amount;

    public Transfer(int transferId, int transferStatus, int transferType, int accountFrom, int accountTo, BigDecimal amount) {
        this.transferId = transferId;
        this.transferStatus = STATUS_OPTIONS[transferStatus - 1];
        this.transferType = TRANSFER_TYPES[transferType - 1];
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

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
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