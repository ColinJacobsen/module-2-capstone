package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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

    public  void printHistory(int accountId, ActiveService activeService) {
        Transfer[] transfers = activeService.transferHistory(accountId);

        System.out.println("\033[32m+---------------+---------------+---------------+---------------+\033[0m");
        System.out.println("\033[32m+---------------+---------\u001B[1m\033[36mTRANSACTIONS\u001B[0m\033[32m----------+---------------+\033[0m");
        System.out.println("\033[32m+---------------+---------------+---------------+---------------+\033[0m");

        for (Transfer transfer : transfers) {
            char debitOrCredit;
            if(transfer.getAccountTo() == accountId){
                debitOrCredit = '-';
            } else {
                debitOrCredit = '+';
            }
            System.out.printf("\033[32m| \u001B[1m\033[36m%-13s\u001B[32m | \u001B[1m\033[36m%-13s\u001B[32m | \u001B[1m\033[36m%-13s\u001B[32m | \u001B[1m\033[36m%-13s\u001B[32m | \033[0m\n",
                    transfer.getTransferId() , transfer.getTransferTypeString(transfer.getTransferType()),
                    transfer.getTransferStatusAsString(transfer.getTransferStatus()),
                    debitOrCredit + " $" + transfer.getAmount());
//                System.out.println("Id: " + transfer.getTransferId() + "  ||   " + transfer.getTransferTypeString(transfer.getTransferType())
//                                 + "  ||   $" + transfer.getAmount() + "  || " + transfer.getTransferStatusAsString(transfer.getTransferStatus()).toUpperCase());
            }
        System.out.println("\033[32m+---------------+---------------+---------------+---------------+\033[0m");
    }

    public int printPendingRequests(Transfer[] transfers, ActiveService activeService) {
        int transferId= 0;
        if (transfers.length > 0) {
            System.out.println("\033[32m+---------------+---------------+\033[0m");
            System.out.println("\033[32m+-------\u001B[1m\033[36mPENDING REQUESTS\u001B[0m\033[32m--------+\033[0m");
            System.out.println("\033[32m+---------------+---------------+\033[0m");
            System.out.printf("\033[32m| \u001B[1m\033[36m%-13s\033[0m\u001B[32m | \u001B[1m\033[36m%-13s\u001B[32m |\033[0m\n", "ID ", "Amount" );
            System.out.println("\033[32m+---------------+---------------+\033[0m");
            //System.out.println("\n\nPENDING REQUESTS\n_____________________________");
            for (Transfer transfer : transfers) {
//            int transferStatus = transfer.getTransferStatus();
                System.out.printf("\033[32m| \u001B[1m\033[36m%-13s\u001B[32m | \u001B[1m\033[36m%-13s\u001B[32m |\033[0m\n", transfer.getTransferId() , "$" +transfer.getAmount() );
//                System.out.println("Id: " + transfer.getTransferId() + "  ||   " + transfer.getTransferTypeString(transfer.getTransferType())
//                        + "  ||   $" + transfer.getAmount() + "  || " + transfer.getTransferStatusAsString(transfer.getTransferStatus()).toUpperCase() + "\n");
            }
            System.out.println("\033[32m+---------------+---------------+\033[0m");
            transferId = promptForInt("Enter the id for the transfer you would like to view: ");

            for (Transfer transfer : transfers) {
                if (transferId == transfer.getTransferId()) {
                    String senderUsername = activeService.accountIdToUsername(transfer.getAccountTo());
                    System.out.println("\033[32m+---------------+---------------+---------------+---------------+\033[0m");
                    System.out.printf("| \u001B[1m\033[36m%-13s\033[0m\u001B[0m | \u001B[1m\033[36m%-13s\033[0m\u001B[0m | \u001B[1m\033[36m%-13s\033[0m\u001B[0m | \u001B[1m\033[36m%-13s\033[0m\u001B[0m |\n", "ID ", "Type", "Requested By", "Amount" ) ;
                    System.out.println("\033[32m+---------------+---------------+---------------+---------------+\033[0m");
                    System.out.printf("| \033[36m%-13s\033[0m | \033[36m%-13s\033[0m | \033[36m%-13s\033[0m | \033[36m%-13s\033[0m |\n", transferId, transfer.getTransferTypeString(transfer.getTransferType()), senderUsername,"$" +transfer.getAmount());
                    System.out.println("\033[32m+---------------+---------------+---------------+---------------+\033[0m");
                }
            }

        } else {
            System.out.println("You have no pending transfer requests");
        }

        return transferId;
    }
}
