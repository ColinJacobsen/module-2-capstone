package com.techelevator.tenmo.consoleGUI;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.services.ActiveService;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SearchBarPanel extends JPanel {
    private final Font MENU_FONT = new Font("Arial", Font.PLAIN, 40);

    private final Font RESULTS_FONT = new Font("Arial", Font.PLAIN, 25);
    private JTextField searchBarTextField;
    private JButton searchButton;
    private JPanel searchPanel;
    private JPanel resultsPanel;
    private List<String> usernamesList = new ArrayList<>();

    private List<User> allUsers = new ArrayList<>();

    private JButton resultsSendButton;
    private JButton resultsRequestButton;
    private JButton resultsAddToContactsButton;

    private ActiveService activeService;
    private AuthenticatedUser currentUser;

    public SearchBarPanel(List<String> usernames, ActiveService activeService, AuthenticatedUser currentUser, List<User> users) {
        this.activeService = activeService;
        this.currentUser = currentUser;
        usernamesList.addAll(usernames);
        allUsers.addAll(users);

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
        resultsPanel = new JPanel() {};
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        //resultsPanel.setPreferredSize(new Dimension(520, 500));
        resultsPanel.setMinimumSize(new Dimension(520,500));
        resultsPanel.setMaximumSize(new Dimension(520,500));
        JScrollPane resultsScrollPane = new JScrollPane(resultsPanel);
        resultsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        resultsScrollPane.setPreferredSize(new Dimension(540, 500));
        resultsPanel.setBackground(new Color(10, 120, 120));

       // resultsPanel.add(resultsScrollPane);
        //resultsScrollPane.add(resultsPanel);


        add(searchPanel);
        add(resultsScrollPane);
    }

    private void updateResults() {

        String searchTerm = searchBarTextField.getText();
        List<String> filteredResults = new ArrayList<>();
        resultsPanel.removeAll();
        for (String result : usernamesList) {
            if (result.toLowerCase().contains(searchTerm.toLowerCase())) {
                filteredResults.add(result);
            }
        }

        if (filteredResults.size() > 0) {
            int colorCounter = 0;
            int resultCounter = 0;

            for (String username : filteredResults) {
                if (resultCounter < 30 && resultCounter < filteredResults.size()) {
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
                    resultsAddToContactsButton.setIcon(new ImageIcon("tenmo-client/src/main/resources/Images/icons8-address-book-25.png"));
                    resultsAddToContactsButton.addActionListener(e -> {
                        int contactId = 0;
                        for(User user : allUsers){
                            if(user.getUsername().equals(username)){
                                contactId = user.getId();
                            }
                        }
                       activeService.addUserToContacts(currentUser.getUser().getId(), contactId);
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

}
