package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import io.cucumber.java.bs.A;

import java.math.BigDecimal;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

    public void printUsers(ActiveService activeService, AuthenticatedUser currentUser) {
        for (User user : activeService.getAllUsers(currentUser)) {
            if (user.getId() == currentUser.getUser().getId()) {
                continue;
            }
            System.out.println(user.getUsername());
        }
    }

    public  int printHistory(int id, ActiveService activeService, int status) {
        Transfer[] transfers = activeService.transferHistory(id);
        System.out.println("\n\nTRANSFERS\n_____________________________");
        for (Transfer transfer : transfers) {
            int transferStatus = transfer.getTransferStatus();
            if (status == 1 && transferStatus == 1) {
                System.out.println("Id: " + transfer.getTransferId() + "  ||   " + transfer.getTransferTypeString(transfer.getTransferType())
                                 + "  ||   $" + transfer.getAmount() + "  || " + transfer.getTransferStatusAsString(transfer.getTransferStatus()).toUpperCase());
            } else if (transferStatus != 1){
                System.out.println("Id: " + transfer.getTransferId() + "  ||   " + transfer.getTransferTypeString(transfer.getTransferType())
                        + "  ||   $" + transfer.getAmount() + "  || " + transfer.getTransferStatusAsString(transfer.getTransferStatus()).toUpperCase());

            }
        }
        int transferId = promptForInt("Enter the id for the transfer you would like to view: ");

        for (Transfer transfer : transfers) {
            if (transferId == transfer.getTransferId()) {
                String senderUsername = activeService.accountIdToUsername(transfer.getAccountFrom());
                String recipientUsername = activeService.accountIdToUsername(transfer.getAccountTo());
                System.out.println("-----------------------------------------------");
                System.out.println("Transfer ID: " + transfer.getTransferId());
                //System.out.println("Transfer status: " + transfer.getTransferStatus());
                System.out.println("Transfer type: " + transfer.getTransferTypeString(transfer.getTransferType()));
                System.out.println("Sending account: " + senderUsername);
                System.out.println("receiving account: " + recipientUsername);
                System.out.println("Transfer amount : $" + transfer.getAmount());
                System.out.println("-----------------------------------------------");
            }
        }
        return transferId;

    }
}
