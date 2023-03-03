package com.techelevator.tenmo.consoleGUI;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ContactsPanel extends JPanel {
    private final Font MENU_FONT = new Font("Arial", Font.PLAIN, 40);
    private final Font RESULTS_FONT = new Font("Arial", Font.PLAIN, 25);
    private JTextField searchBarTextField;
    private JPanel searchPanel;
    private JPanel contactsPanel;
    private java.util.List<String> resultsList;
    private List<String> contactsList = new ArrayList<String>();

    private JButton contactsSendButton;
    private JButton contactsRequestButton;
    private JButton contactsDeleteFromContactsButton;

    int colorCounter = 0;

    int resultCounter = 0;

    public ContactsPanel(List<String> contactUsernames) {
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
        //searchPanel.setSize(500,300);
        searchPanel.add(searchBarTextField);
        //searchPanel.add(searchButton);

        contactsPanel = new JPanel() {
        };
        contactsPanel.setLayout(new BoxLayout(contactsPanel, BoxLayout.Y_AXIS));
        contactsPanel.setPreferredSize(new Dimension(535, 510));
        JScrollPane resultsScrollPane = new JScrollPane();
        JViewport viewport = resultsScrollPane.getViewport();
        viewport.setBackground(new Color(175, 255, 200));
        resultsScrollPane.setPreferredSize(new Dimension(525, 500));
        contactsPanel.setBackground(new Color(10, 120, 120));

        contactsPanel.add(resultsScrollPane);

        add(searchPanel);
        add(contactsPanel);


        for (String username : contactUsernames) {

            JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0)) {
            };
            usernamePanel.setPreferredSize(new Dimension(525, 40));
            usernamePanel.setMaximumSize(new Dimension(525, 40));
            usernamePanel.setMaximumSize(new Dimension(525, 40));
            if (colorCounter % 2 == 0) {
                usernamePanel.setBackground(new Color(100, 255, 180));
            } else {
                usernamePanel.setBackground(new Color(100, 255, 200));
            }
            JLabel usernameLabel = new JLabel(username);
            usernameLabel.setFont(RESULTS_FONT);
            usernamePanel.add(usernameLabel);
            usernameLabel.setPreferredSize(new Dimension(395, 40));
            contactsSendButton = new JButton();
            contactsSendButton.setIcon(new ImageIcon("tenmo-client/src/main/resources/Images/icons8-money-transfer-25.png"));
            contactsRequestButton = new JButton();
            contactsRequestButton.setIcon(new ImageIcon("tenmo-client/src/main/resources/Images/icons8-request-money-25.png"));
            contactsDeleteFromContactsButton = new JButton();
            contactsDeleteFromContactsButton.setIcon(new ImageIcon("tenmo-client/src/main/resources/Images/icons8-close-25.png"));

            contactsSendButton.setPreferredSize(new Dimension(35, 35));
            contactsRequestButton.setPreferredSize(new Dimension(35, 35));
            contactsDeleteFromContactsButton.setPreferredSize(new Dimension(35, 35));

            usernamePanel.add(contactsSendButton);
            usernamePanel.add(contactsRequestButton);
            usernamePanel.add(contactsDeleteFromContactsButton);

            contactsPanel.add(usernamePanel);

        }
    }

    private void updateResults() {

        String searchTerm = searchBarTextField.getText();
        List<String> filteredResults = new ArrayList<>();
        contactsPanel.removeAll();
        for (String result : contactsList) {
            if (result.toLowerCase().contains(searchTerm.toLowerCase())) {
                filteredResults.add(result);
            }
        }

        if (filteredResults.size() > 0) {
            colorCounter = 0;
            resultCounter = 0;

            for (String username : filteredResults) {
                if (resultCounter < 14 && resultCounter < filteredResults.size()) {
                    JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0)) {
                    };
                    usernamePanel.setPreferredSize(new Dimension(525, 40));
                    usernamePanel.setMaximumSize(new Dimension(525, 40));
                    usernamePanel.setMaximumSize(new Dimension(525, 40));
                    if (colorCounter % 2 == 0) {
                        usernamePanel.setBackground(new Color(100, 255, 180));
                    } else {
                        usernamePanel.setBackground(new Color(100, 255, 200));
                    }
                    JLabel usernameLabel = new JLabel(username);
                    usernameLabel.setFont(RESULTS_FONT);
                    usernamePanel.add(usernameLabel);
                    usernameLabel.setPreferredSize(new Dimension(395, 40));
                    contactsSendButton = new JButton();
                    contactsSendButton.setIcon(new ImageIcon("tenmo-client/src/main/resources/Images/icons8-money-transfer-25.png"));
                    contactsRequestButton = new JButton();
                    contactsRequestButton.setIcon(new ImageIcon("tenmo-client/src/main/resources/Images/icons8-request-money-25.png"));
                    contactsDeleteFromContactsButton = new JButton();
                    contactsDeleteFromContactsButton.setIcon(new ImageIcon("tenmo-client/src/main/resources/Images/icons8-close-25.png"));

                    contactsSendButton.setPreferredSize(new Dimension(35, 35));
                    contactsRequestButton.setPreferredSize(new Dimension(35, 35));
                    contactsDeleteFromContactsButton.setPreferredSize(new Dimension(35, 35));

                    usernamePanel.add(contactsSendButton);
                    usernamePanel.add(contactsRequestButton);
                    usernamePanel.add(contactsDeleteFromContactsButton);

                    contactsPanel.add(usernamePanel);
                    colorCounter++;
                    resultCounter++;
                }
            }
        }
        contactsPanel.revalidate();
        contactsPanel.repaint();
    }

}
