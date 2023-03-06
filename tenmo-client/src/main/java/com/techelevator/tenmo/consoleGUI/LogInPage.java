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
    Image backgroundImage = Toolkit.getDefaultToolkit().getImage("tenmo-client/src/main/resources/Images/Backgrounds/LogInBackground small.png");
    ImageIcon backgroundIcon = new ImageIcon((backgroundImage.getScaledInstance(520, 600, Image.SCALE_SMOOTH)));
    JLabel backGroundLabel = new JLabel(backgroundIcon);
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private AuthenticatedUser currentUser;
    //private final String token = currentUser.getToken();
    private final ActiveService activeService = new ActiveService(API_BASE_URL, currentUser);
    private final TransferService transferService = new TransferService(API_BASE_URL, currentUser);


    JLabel passwordLabel;
    JPasswordField passwordField;
    JTextField userNameTextField;

    public LogInPage() {
        setContentPane(backGroundLabel);


        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(520, 600);
        setLayout(null);
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setSize(75,40);
        usernameLabel.setLocation(90,200);
        userNameTextField = new JTextField(30);
        userNameTextField.setSize(200,40);
        userNameTextField.setLocation(160, 200);

        passwordLabel = new JLabel("Password:");
        passwordLabel.setSize(75,40);
        passwordLabel.setLocation(90,300);

        passwordField = new JPasswordField(20);
        passwordField.setLocation(160, 300);
        passwordField.setSize(200,40);


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
