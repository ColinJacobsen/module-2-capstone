package com.techelevator.tenmo.consoleGUI;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.services.ActiveService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.TransferService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContactsPanel extends JPanel {
    private final Font MENU_FONT = new Font("Arial", Font.PLAIN, 40);
    private final Font RESULTS_FONT = new Font("Arial", Font.PLAIN, 25);
    private TransferService transferService;
    private ActiveService activeService;
    private AuthenticatedUser currentUser;

    private List<Integer> contactIds= new ArrayList<>();
    private List<String> contactUsernames = new ArrayList<>();
    private List<String> allUsernames = new ArrayList<>();
    private JTextField searchBarTextField;
    private JPanel searchPanel;
    private JPanel contactsPanel;
    private List<User> allUsers = new ArrayList<>();
    private JButton contactsSendButton;
    private JButton contactsRequestButton;
    private JButton contactsDeleteFromContactsButton;
    JScrollPane resultsScrollPane;

    int currentUserId;

    //int colorCounter = 0;

    int resultCounter = 0;

    public ContactsPanel( ActiveService activeService, TransferService transferService, AuthenticatedUser currentUser) {
        this.activeService = activeService;
        this.transferService = transferService;
        this.currentUser = currentUser;
        currentUserId = currentUser.getUser().getId();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                refreshLists();
                updateResults();
            }
        });
        // Build Panel
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        searchPanel = new JPanel();

        searchBarTextField = new JTextField(15);
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
        contactsPanel = new JPanel() {
        };
        contactsPanel.setLayout(new BoxLayout(contactsPanel, BoxLayout.Y_AXIS));
        resultsScrollPane = new JScrollPane(contactsPanel);
        resultsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        resultsScrollPane.setPreferredSize(new Dimension(540, 500));
        contactsPanel.setBackground(new Color(10, 120, 120));

        add(searchPanel);
        add(resultsScrollPane);

    }

    private void updateResults() {

        String searchTerm = searchBarTextField.getText();
        List<String> filteredResults = new ArrayList<>();
        contactsPanel.removeAll();
        filteredResults.addAll(contactUsernames);
//        for (String username : contactUsernames) {
//            if (username.toLowerCase().contains(searchTerm.toLowerCase())) {
//                filteredResults.add(username);
//            }
//        }
        Collections.sort(filteredResults);

        if (filteredResults.size() > 0) {
            int colorCounter = 0;

            for (String username : filteredResults) {
                JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0)) {
                };
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

                contactsSendButton = new JButton();
                contactsSendButton.setIcon(new ImageIcon("tenmo-client/src/main/resources/Images/icons8-money-transfer-25.png"));
                contactsSendButton.addActionListener(e -> {

                });

                contactsRequestButton = new JButton();
                contactsRequestButton.setIcon(new ImageIcon("tenmo-client/src/main/resources/Images/icons8-request-money-25.png"));
                contactsRequestButton.addActionListener(e -> {

                });

                contactsDeleteFromContactsButton = new JButton();

                contactsDeleteFromContactsButton.setIcon(new ImageIcon("tenmo-client/src/main/resources/Images/icons8-close-25.png"));
//                User finalContactToDelete = contactToDelete;

                contactsDeleteFromContactsButton.addActionListener(e -> {
                    int contactId = 0;
                    for (User user : allUsers) {
                        if (user.getUsername().equals(username)) {
                            contactId = user.getId();
                        }
                    }
                    activeService.deleteUserFromContacts(currentUserId, contactId);
                    refreshLists();
                    updateResults();
                });

                contactsSendButton.setPreferredSize(new Dimension(35, 35));
                contactsRequestButton.setPreferredSize(new Dimension(35, 35));
                contactsDeleteFromContactsButton.setPreferredSize(new Dimension(35, 35));

                usernamePanel.add(contactsSendButton);
                usernamePanel.add(contactsRequestButton);
                usernamePanel.add(contactsDeleteFromContactsButton);

                contactsPanel.add(usernamePanel);
                colorCounter++;
            }
        }


            contactsPanel.revalidate();
            contactsPanel.repaint();
    }


    public void refreshLists() {
        allUsers.clear();
        contactIds.clear();
        contactUsernames.clear();
        allUsernames.clear();

        allUsers.addAll(activeService.getAllUsers(currentUser));

        contactIds.addAll(activeService.getContactsList(currentUser.getUser().getId()));

        for (User user : allUsers) {
            if (contactIds.contains(user.getId())) {
                contactUsernames.add(user.getUsername());
            }
        }
        for (User user : allUsers) {
            allUsernames.add(user.getUsername());
        }
    }

}


//    public void printResults(List<String> results, int limit) {
//        int colorCounter = 0;
//        int limitCounter = 0;
//        if (limitCounter < limit && limitCounter < results.size())
//            for (String username : results) {
//                JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0)) {
//                };
//                usernamePanel.setPreferredSize(new Dimension(525, 40));
//                usernamePanel.setMaximumSize(new Dimension(525, 40));
//                usernamePanel.setMaximumSize(new Dimension(525, 40));
//                if (colorCounter % 2 == 0) {
//                    usernamePanel.setBackground(new Color(100, 255, 180));
//                } else {
//                    usernamePanel.setBackground(new Color(100, 255, 200));
//                }
//                JLabel usernameLabel = new JLabel(username);
//                usernameLabel.setFont(RESULTS_FONT);
//                usernamePanel.add(usernameLabel);
//                usernameLabel.setPreferredSize(new Dimension(390, 40));
//                contactsSendButton = new JButton();
//                contactsSendButton.setIcon(new ImageIcon("tenmo-client/src/main/resources/Images/icons8-money-transfer-25.png"));
//                contactsRequestButton = new JButton();
//                contactsRequestButton.setIcon(new ImageIcon("tenmo-client/src/main/resources/Images/icons8-request-money-25.png"));
//                contactsDeleteFromContactsButton = new JButton();
//                contactsDeleteFromContactsButton.setIcon(new ImageIcon("tenmo-client/src/main/resources/Images/icons8-close-25.png"));
//                contactsDeleteFromContactsButton.addActionListener(e -> {
//                    System.out.println("Delete Button Pressed");
//                });
//                contactsSendButton.setPreferredSize(new Dimension(35, 35));
//                contactsRequestButton.setPreferredSize(new Dimension(35, 35));
//                contactsDeleteFromContactsButton.setPreferredSize(new Dimension(35, 35));
//
//                usernamePanel.add(contactsSendButton);
//                usernamePanel.add(contactsRequestButton);
//                usernamePanel.add(contactsDeleteFromContactsButton);
//
//                resultsScrollPane.add(usernamePanel);
//                colorCounter++;
//                limitCounter++;
//            }
//
//        contactsPanel.revalidate();
//        contactsPanel.repaint();
//    }
//}
//


