package com.techelevator.tenmo.consoleGUI;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.ActiveService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogInPage extends JFrame {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private AuthenticatedUser currentUser;
    //private final String token = currentUser.getToken();
    private final ActiveService activeService = new ActiveService(API_BASE_URL, currentUser);
    private final TransferService transferService = new TransferService(API_BASE_URL, currentUser);


    JLabel passwordLabel;
    JPasswordField passwordField;
    JTextField userNameTextField;

    public LogInPage() {


        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(540, 600);
        setLayout(null);
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setSize(100,50);
        usernameLabel.setLocation(50,100);
        userNameTextField = new JTextField(30);
        userNameTextField.setSize(100,50);
        userNameTextField.setLocation(160, 100);

        passwordLabel = new JLabel("Password:");
        passwordLabel.setSize(100,50);
        passwordLabel.setLocation(50,300);

        passwordField = new JPasswordField(20);
        passwordField.setLocation(160, 300);
        passwordField.setSize(100,50);


        JButton signInButton = new JButton("Sign In");
        signInButton.setLocation(220, 400);
        signInButton.setSize(100,50);
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guiLogIn();
            }
        });

        JLabel registerLabel = new JLabel("Don't have an account? ");
        JButton registerButton = new JButton("Register");

        add(usernameLabel);
        add(userNameTextField);
        add(passwordLabel);
        add(passwordField);
        add(signInButton);

        setVisible(true);
//        registerButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                System.out.println("Open Registration Window Goes Here");
//            }
//        });

    }

    public void guiLogIn() {

        String userName = userNameTextField.getText();
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);
        UserCredentials userCredentials = new UserCredentials(userName, password);
        currentUser = authenticationService.login(userCredentials);
        transferService.setCurrentUser(currentUser);
        transferService.setAuthToken(currentUser.getToken());
        if (currentUser != null) {
            setVisible(false);
            ConsoleGUI gui = new ConsoleGUI(currentUser);
        } else {
            System.out.println("Error check log");
        }
    }
}
