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
import java.util.ArrayList;
import java.util.List;

public class ContactsPanel extends JPanel {
    private final Font MENU_FONT = new Font("Arial", Font.PLAIN, 40);
    private final Font RESULTS_FONT = new Font("Arial", Font.PLAIN, 25);
    private final ActiveService activeService;
    private final Object currentUser;
    private ArrayList<String> usernamesList = new ArrayList<>();
    private JTextField searchBarTextField;
    private JPanel searchPanel;
    private JPanel contactsPanel;
    private List<String> contactsList = new ArrayList<String>();

    private List<User> allUsers = new ArrayList<>();
    private JButton contactsSendButton;
    private JButton contactsRequestButton;
    private JButton contactsDeleteFromContactsButton;
    JScrollPane resultsScrollPane;

    int currentUserId;

    //int colorCounter = 0;

    int resultCounter = 0;

    public ContactsPanel(List<String> contactUsernames, ActiveService activeService, TransferService transferService, AuthenticatedUser currentUser, List<User> users) {
        this.activeService = activeService;
        this.currentUser = currentUser;
        usernamesList.addAll(contactUsernames);
        allUsers.addAll(users);
        currentUserId = currentUser.getUser().getId();

        if (contactUsernames.size() > 0) {
            contactsList.addAll(contactUsernames);
        }
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        searchPanel = new JPanel();

        searchBarTextField = new JTextField(15);
        searchBarTextField.setFont(MENU_FONT);
        searchBarTextField.setForeground(new Color(50, 60, 60));
        searchBarTextField.setPreferredSize(new Dimension(100, 40));

        searchBarTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateAndPrintResults();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateAndPrintResults();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateAndPrintResults();
            }
        });

        searchPanel.setBackground(new Color(50, 150, 100));
        searchPanel.add(searchBarTextField);
        contactsPanel = new JPanel() {
        };
        contactsPanel.setLayout(new BoxLayout(contactsPanel, BoxLayout.Y_AXIS));
        //contactsPanel.setPreferredSize(new Dimension(520, 480));
        resultsScrollPane = new JScrollPane(contactsPanel);
        resultsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        resultsScrollPane.setPreferredSize(new Dimension(540, 500));
        contactsPanel.setBackground(new Color(10, 120, 120));

//        contactsPanel.add(resultsScrollPane);

        add(searchPanel);
        add(resultsScrollPane);

        //printResults(contactUsernames, 1000);
    }

    private void updateAndPrintResults() {

        String searchTerm = searchBarTextField.getText();
        List<String> filteredResults = new ArrayList<>();
        contactsPanel.removeAll();
        for (String result : contactsList) {
            if (result.toLowerCase().contains(searchTerm.toLowerCase())) {
                filteredResults.add(result);
            }
        }

        if (filteredResults.size() > 0) {
            int colorCounter = 0;

            for (String username : filteredResults) {

                int contactToDeleteId = 0;

                for(User user: allUsers){
                    if(user.getUsername().equals(username)){
                        contactToDeleteId = user.getId();
                    }
                }
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


                contactsRequestButton = new JButton();
                contactsRequestButton.setIcon(new ImageIcon("tenmo-client/src/main/resources/Images/icons8-request-money-25.png"));


                contactsDeleteFromContactsButton = new JButton();
                contactsDeleteFromContactsButton.setIcon(new ImageIcon("tenmo-client/src/main/resources/Images/icons8-close-25.png"));
                int finalContactToDeleteId = contactToDeleteId;
                contactsDeleteFromContactsButton.addActionListener(e -> {
                    activeService.deleteUserFromContacts(currentUserId, finalContactToDeleteId);
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

            contactsPanel.revalidate();
            contactsPanel.repaint();
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


