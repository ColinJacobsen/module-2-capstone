package com.techelevator.tenmo.consoleGUI;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.services.ActiveService;
import com.techelevator.tenmo.services.TransferService;
import io.cucumber.java.bs.A;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

public class ViewTransfersPanel extends JPanel {

    private JPanel transfersDisplayPanel;
    private ActiveService activeService;
    private TransferService transferService;
    private AuthenticatedUser currentUser;
    java.util.List<Transfer> sentTransfers = new ArrayList<>();
    java.util.List<Transfer> transfersSentToUser = new ArrayList<>();
    java.util.List<Transfer> sentRequestsStillPending = new ArrayList<>();
    java.util.List<Transfer> sentRequestsApproved = new ArrayList<>();
    java.util.List<Transfer> sentRequestsRejected = new ArrayList<>();
    java.util.List<Transfer> receivedRequestsPending = new ArrayList<>();
    java.util.List<Transfer> receivedRequestsApproved = new ArrayList<>();
    java.util.List<Transfer> receivedRequestsRejected = new ArrayList<>();

    private java.util.List<Transfer> transfers = new ArrayList<>();
    private JPanel transferPanel;

    public ViewTransfersPanel(ActiveService activeService, TransferService transferService, AuthenticatedUser currentUser) {

        this.activeService = activeService;
        this.transferService = transferService;
        this.currentUser = currentUser;

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                    //transfers.addAll(transferService.getAllUserTransfers(activeService.userToAccount(currentUser.getUser())));
                    createAndSortTransactions(currentUser.getUser().getId());
                    printTransfersToScreen(transfers);
                    for(Transfer transfer : transfers){
                        System.out.println(transfer.toString());
                    }
            }
        });

        ///BUILD PANEL

        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        transfersDisplayPanel = new JPanel();

        transfersDisplayPanel.setLayout(new BoxLayout(transfersDisplayPanel, BoxLayout.Y_AXIS));
        transfersDisplayPanel.setMinimumSize(new Dimension(500, 500));
        transfersDisplayPanel.setMaximumSize(new Dimension(500, 500));
        JScrollPane resultsScrollPane = new JScrollPane(transfersDisplayPanel);
        resultsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        resultsScrollPane.setPreferredSize(new Dimension(540, 500));
        transfersDisplayPanel.setBackground(new Color(10, 120, 120));

        add(resultsScrollPane);

    }

    public void createAndSortTransactions(int userId) {
        sentTransfers.clear();
        transfersSentToUser.clear();
        sentRequestsStillPending.clear();
        sentRequestsApproved.clear();
        sentRequestsRejected.clear();
        receivedRequestsPending.clear();
        receivedRequestsApproved.clear();
        receivedRequestsRejected.clear();
        transfers.clear();

        transfers.addAll(transferService.getAllUserTransfers(activeService.userToAccount(currentUser.getUser())));
        for (Transfer transfer : transfers) {
            if (transfer.getTransferType() == 1) {              // IF THE TRANSFER TYPE IS REQUESTED------------------------------- TYPE CHECK
                if (transfer.getTransferStatus() == 1) {         //IF THE TRANSFER IS PENDING ------------------------------------Status Check
                    if (transfer.getAccountTo() == currentUser.getUser().getId()) { // IF REQUEST SENT BY USER --------------Sender Check
                        sentRequestsStillPending.add(transfer);
                    } else {                                                            //ELSE THE USER WAS RECIPIENT OF REQUEST
                        receivedRequestsPending.add(transfer);
                    }
                } else if (transfer.getTransferStatus() == 2) {
                    if (transfer.getAccountTo() == currentUser.getUser().getId()) {//----- REQUEST SENT BY USER AND APPROVED
                        sentRequestsApproved.add(transfer);
                    } else {                                                            /// REQUEST SENT TO USER and APPROVED
                        receivedRequestsApproved.add(transfer);
                    }
                } else if (transfer.getTransferStatus() == 3) {
                    if (transfer.getAccountTo() == currentUser.getUser().getId()) {//----- REQUEST SENT BY USER AND APPROVED
                        sentRequestsRejected.add(transfer);
                    } else {                                                            /// REQUEST SENT TO USER and APPROVED
                        receivedRequestsRejected.add(transfer);
                    }
                }
            } else {                                             // IF THE TRANSFER TYPE IS SENT---------------------------- TYPE CHECK
                if (transfer.getAccountFrom() == currentUser.getUser().getId()) {   //IF THE SENDER IS USER
                    sentTransfers.add(transfer);
                } else {
                    transfersSentToUser.add(transfer);
                }
            }


        }
    }

    public java.util.List<Transfer> returnTransactionsOfType(int type, int status, boolean didUserSend) {
        if (type == 1) {
            if (status == 1) { //REQUESTED TRANSFERS
                if (didUserSend) {
                    return sentRequestsStillPending;
                } else {
                    return receivedRequestsPending;
                }
            } else if (status == 2) {
                if (didUserSend) {
                    return sentRequestsApproved;
                } else {
                    return receivedRequestsApproved;
                }
            } else if (status == 3) {
                if (didUserSend) {
                    return sentRequestsRejected;
                } else {
                    return receivedRequestsRejected;
                }
            }
        } else if (status == 2) { //SENT TRANSFERS
            if (didUserSend) {
                return sentTransfers;
            } else {
                return transfersSentToUser;
            }
        }
        return new ArrayList<>();
    }

    public void printTransfersToScreen(java.util.List<Transfer> transfersToPrint){
        int colorCounter = 0;
        int resultCounter = 0;

        transfersDisplayPanel.removeAll();

        for(Transfer transfer: transfersToPrint){
            JPanel transferPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
            //JLabel transferLabel = new JLabel(transfer.toLabelString());
            //mapTransferToPanel(transfer);
            transferPanel.setPreferredSize(new Dimension(520, 40));
            transferPanel.setMaximumSize(new Dimension(520, 40));
            transferPanel.setMaximumSize(new Dimension(520, 40));
            //transferPanel.add(transferLabel);

            if (colorCounter % 2 == 0) {
                transferPanel.setBackground(new Color(100, 255, 180));
            } else {
                transferPanel.setBackground(new Color(100, 255, 200));
            }
            JLabel transferLabel = new JLabel(transfer.toLabelString());
            transferLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            transferLabel.setPreferredSize(new Dimension(480,40));
            transferPanel.add(transferLabel);

            transfersDisplayPanel.add(transferPanel);
            colorCounter++;
            resultCounter++;

        }
        transfersDisplayPanel.revalidate();
        transfersDisplayPanel.repaint();
    }
    public JPanel mapTransferToPanel(Transfer transfer){
        JLabel transferIdLabel = new JLabel(String.valueOf(transfer.getTransferId()));
        JLabel transferType = new JLabel(transfer.getTransferTypeString(transfer.getTransferType()));
        JLabel transferStatus = new JLabel(transfer.getTransferStatusAsString(transfer.getTransferStatus()));
        JLabel transferAmount = new JLabel(String.valueOf(transfer.getAmount()));

        transferPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        transferPanel.add(transferIdLabel);
//        transferPanel.add(transferType);
//        transferPanel.add(transferStatus);
//        transferPanel.add(transferAmount);

        return transferPanel;
    }

}


