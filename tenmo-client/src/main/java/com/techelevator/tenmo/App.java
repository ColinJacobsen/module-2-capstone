package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.ActiveService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;

import java.math.BigDecimal;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;
    private final ActiveService activeService = new ActiveService(API_BASE_URL, currentUser);
    private final TransferService transferService = new TransferService(API_BASE_URL);

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        transferService.setCurrentUser(currentUser);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    private void viewCurrentBalance() {
        // TODO Auto-generated method stub
        activeService.setCurrentUser(currentUser);
        System.out.println(activeService.getUserBalance(currentUser, currentUser.getUser().getId()));

    }

    private void viewTransferHistory() {
        // TODO Auto-generated method stub
        consoleService.printHistory(activeService.userToAccount(currentUser.getUser()), activeService, 2);





    }

    private void viewPendingRequests() {
        // TODO Auto-generated method stub

        int id = consoleService.printHistory(activeService.userToAccount(currentUser.getUser()), activeService, 1);
        String response = consoleService.promptForString("Would you like to approve this transfer? (Y/N): ").toUpperCase();
        if(response.equals("Y")){
            transferService.doTransfer(transferService.getTransferByTransferId(id));
        }
    }

    private void sendBucks() {
        // TODO Auto-generated method stub
        activeService.setCurrentUser(currentUser);
        consoleService.printUsers(activeService, currentUser);
        String recipient = consoleService.promptForString("Type the name of the account you would like to send to: ");
        if (recipient.toLowerCase().equals(currentUser.getUser().getUsername())) {
            System.err.println("\nYou can not send to yourself");
        } else {

            BigDecimal amount = consoleService.promptForBigDecimal("How much would you like to send?: ");
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.err.println("Value must be more than $0.00");
            } else {
                Transfer transfer = new Transfer(2, 2, activeService.userToAccount(currentUser.getUser()), activeService.userToAccount(activeService.getUserByName(recipient)), amount);

                if (activeService.getAccountBalance(transfer.getAccountFrom()).compareTo(amount) >= 0) {
                    transfer.setTransferId(transferService.makeTransfer(transfer, currentUser));
                    System.out.println(transferService.doTransfer(transfer));

                } else {
                    consoleService.printMainMenu();
                    System.err.println("Insufficient Balance");
                }
            }
        }
    }

    private void requestBucks() {
        // TODO Auto-generated method stub
        activeService.setCurrentUser(currentUser);
        consoleService.printUsers(activeService, currentUser);
        String recipient = consoleService.promptForString("Type the name of the account you would like to send request to: ");
        if (recipient.toLowerCase().equals(currentUser.getUser().getUsername())) {
            System.err.println("\nYou can not request from yourself");
        } else {

            BigDecimal amount = consoleService.promptForBigDecimal("How much would you like to request?: ");
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.err.println("Value must be more than $0.00");
            } else {
                Transfer transfer = new Transfer(1, 1, activeService.userToAccount(activeService.getUserByName(recipient)), activeService.userToAccount(currentUser.getUser()), amount);

                transfer.setTransferId(transferService.makeTransfer(transfer, currentUser));
                if (transfer.getTransferId() > 3000) {
                    System.out.println("\nRequest Sent to " + recipient);
                }
            }

        }
    }
}
