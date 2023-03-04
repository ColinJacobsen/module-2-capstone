package com.techelevator.tenmo.consoleGUI;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.services.ActiveService;
import com.techelevator.tenmo.services.TransferService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchBarPanel extends JPanel {
    private final Font MENU_FONT = new Font("Arial", Font.PLAIN, 40);

    private final Font RESULTS_FONT = new Font("Arial", Font.PLAIN, 25);
    private int currentUserId;
    private List<String> contactUsernames = new ArrayList<>();
    private List<String> allUsernames = new ArrayList<>();
    private List<String> resultsUsernames = new ArrayList<>();
    private List<User> resultsUserList = new ArrayList<>();
    private List<Integer> resultsIds = new ArrayList<>();
    private TransferService transferService;
    private JTextField searchBarTextField;
    private JPanel searchPanel;
    private JPanel resultsPanel;
    private List<String> usernamesList = new ArrayList<>();

    private List<User> allUsers = new ArrayList<>();

    private JButton resultsSendButton;
    private JButton resultsRequestButton;
    private JButton resultsAddToContactsButton;

    private ActiveService activeService;
    private AuthenticatedUser currentUser;

    public SearchBarPanel(ActiveService activeService, TransferService transferService, AuthenticatedUser currentUser) {
        this.activeService = activeService;
        this.transferService = transferService;
        this.currentUser = currentUser;
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                refreshLists();
                updateResults();
            }
        });

        currentUserId = currentUser.getUser().getId();
        ///Build the panel
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        searchPanel = new JPanel();

        searchBarTextField = new JTextField(13);
        searchBarTextField.setFont(MENU_FONT);
        searchBarTextField.setForeground(new Color(50, 60, 60));
        searchBarTextField.setPreferredSize(new Dimension(100, 40));

        searchBarTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateResults();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateResults();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateResults();
            }
        });


        searchPanel.setBackground(new Color(50, 150, 100));
        searchPanel.add(searchBarTextField);

        //************************************RESULTS PANEL
        resultsPanel = new JPanel() {
        };
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setMinimumSize(new Dimension(520, 500));
        resultsPanel.setMaximumSize(new Dimension(520, 500));
        JScrollPane resultsScrollPane = new JScrollPane(resultsPanel);
        resultsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        resultsScrollPane.setPreferredSize(new Dimension(540, 500));
        resultsPanel.setBackground(new Color(10, 120, 120));

        add(searchPanel);
        add(resultsScrollPane);
    }

    private void updateResults() {

        String searchTerm = searchBarTextField.getText();
        List<String> filteredResults = new ArrayList<>();
        resultsPanel.removeAll();
        for (String username : allUsernames) {
            if (username.toLowerCase().contains(searchTerm.toLowerCase())) {
                filteredResults.add(username);
            }
        }
        Collections.sort(filteredResults);

        if (filteredResults.size() > 0) {
            int colorCounter = 0;
            int resultCounter = 0;

            for (String username : filteredResults) {
                if (resultCounter < 30 && resultCounter < filteredResults.size()) {
                    JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0)) {};
                    usernamePanel.setPreferredSize(new Dimension(520, 40));
                    usernamePanel.setMaximumSize(new Dimension(520, 40));
                    usernamePanel.setMaximumSize(new Dimension(520, 40));
                    if (colorCounter % 2 == 0) {
                        usernamePanel.setBackground(new Color(100, 255, 180));
                    } else {
                        usernamePanel.setBackground(new Color(100, 255, 200));
                    }
                    JLabel usernameLabel = new JLabel(username);
                    usernameLabel.setFont(RESULTS_FONT);
                    usernamePanel.add(usernameLabel);
                    usernameLabel.setPreferredSize(new Dimension(375, 40));

                    resultsSendButton = new JButton();
                    resultsSendButton.setIcon(new ImageIcon("tenmo-client/src/main/resources/Images/icons8-money-transfer-25.png"));
                    resultsSendButton.addActionListener(e -> {
                        System.out.println("Set Up Send Transfer transition here");
                    });

                    resultsRequestButton = new JButton();
                    resultsRequestButton.setIcon(new ImageIcon("tenmo-client/src/main/resources/Images/icons8-request-money-25.png"));
                    resultsRequestButton.addActionListener(e -> {
                        System.out.println("Set Up Request Transfer transition here");
                    });

                    resultsAddToContactsButton = new JButton();
                    if (contactUsernames.contains(username)) {
                        resultsAddToContactsButton.setIcon(new ImageIcon("tenmo-client/src/main/resources/Images/icons8-friends-25.png"));
                    } else {
                        resultsAddToContactsButton.setIcon(new ImageIcon("tenmo-client/src/main/resources/Images/icons8-address-book-25.png"));
                    }
                    resultsAddToContactsButton.addActionListener(e -> {
                        int contactId = 0;
                        for (User user : allUsers) {
                            if (user.getUsername().equals(username)) {
                                contactId = user.getId();
                            }
                        }
                        if (contactUsernames.contains(username)) {
                            System.out.println("This user is already in your contacts");
                        } else {
                            activeService.addUserToContacts(currentUserId, contactId);
                            refreshLists();
                            updateResults();
                        }
                    });

                    resultsSendButton.setPreferredSize(new Dimension(35, 35));
                    resultsRequestButton.setPreferredSize(new Dimension(35, 35));
                    resultsAddToContactsButton.setPreferredSize(new Dimension(35, 35));

                    usernamePanel.add(resultsSendButton);
                    usernamePanel.add(resultsRequestButton);
                    usernamePanel.add(resultsAddToContactsButton);

                    resultsPanel.add(usernamePanel);
                    colorCounter++;
                    resultCounter++;
                }
            }
        }
        resultsPanel.revalidate();
        resultsPanel.repaint();

    }

    public void refreshLists() {
        allUsers.clear();
        resultsIds.clear();
        resultsUserList.clear();
        contactUsernames.clear();
        allUsernames.clear();

        allUsers.addAll(activeService.getAllUsers(currentUser));
        resultsIds.addAll(activeService.getContactsList(currentUserId));

        for (User user : allUsers) {
            allUsernames.add(user.getUsername());
            if (resultsIds.contains(user.getId())) {
                resultsUserList.add(user);
                resultsUsernames.add(user.getUsername());
            }
            if (activeService.getContactsList(currentUserId).contains(user.getId())) {
                contactUsernames.add(user.getUsername());
            }
        }
    }
}
