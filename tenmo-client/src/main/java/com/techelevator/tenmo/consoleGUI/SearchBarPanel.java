package com.techelevator.tenmo.consoleGUI;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.Document;

public class SearchBarPanel extends JPanel {
    private final Dimension MENU_OPTION_DIMENSION = new Dimension(540, 125);
    private final Font MENU_FONT = new Font("Arial", Font.PLAIN, 40);
    private JTextField searchBarTextField;
    private JButton searchButton;
    private JPanel searchPanel;
    private JPanel resultsPanel;
    private List<String> resultsList;
    private List<String> usernamesList = new ArrayList<>();

    public SearchBarPanel(List<String> usernames) {
        usernamesList.addAll(usernames);

        setLayout(new FlowLayout(FlowLayout.CENTER, 10,10));
        searchPanel = new JPanel();

        searchBarTextField = new JTextField(13);
        searchBarTextField.setFont(MENU_FONT);
        searchBarTextField.setForeground(new Color(50,60,60));
        searchBarTextField.setPreferredSize(new Dimension(100,40));

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

        //searchBar.setBounds(new Dimension(300, 80));;
        searchButton = new JButton(new ImageIcon("C:\\Users\\Kenny\\Desktop\\meritamerica\\Module2\\module-2-capstone\\tenmo-client\\src\\main\\resources\\Images\\icons8-search-50.png"));
        //searchButton.setBackground(new Color(100, 255, 175));
        searchButton.setPreferredSize(new Dimension(55,55));
        searchButton.setBackground(new Color(100,222,150));

        searchPanel.setBackground(new Color(50,150,100));
        //searchPanel.setSize(500,300);
        searchPanel.add(searchBarTextField);
        searchPanel.add(searchButton);

        resultsPanel = new JPanel(){};
        resultsPanel.setPreferredSize(new Dimension(535,510));
        JScrollPane resultsScrollPane = new JScrollPane();
        JViewport viewport = resultsScrollPane.getViewport();
        viewport.setBackground(new Color(175,255,200));
        resultsScrollPane.setPreferredSize(new Dimension(525,500));
        resultsPanel.setBackground(new Color(10,120,120));

        resultsPanel.add(resultsScrollPane);

        JButton profileSendButton = new JButton();
        JButton profileRequestButton = new JButton();
        JButton profileAddToContactsButton = new JButton();

//        if(results.size()>0){
//            int colorCounter=0;
//            for(String username : usernames){
//                JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5,1)){};
//                usernamePanel.setSize(505,30);
//                if(colorCounter%2 ==0){
//                    usernamePanel.setBackground(new Color(100,255,180));
//                } else {
//                    usernamePanel.setBackground(new Color(100,255,200));
//                }
//                JLabel usernameLabel = new JLabel(username);
//                usernamePanel.add(usernameLabel);
//                resultsPanel.add(usernamePanel);
//                colorCounter ++;
//
//            }
//        }

        add(searchPanel);
        add(resultsPanel);
    }

    private void updateResults() {

        String searchTerm = searchBarTextField.getText();
        List<String> filteredResults = new ArrayList<>();
        resultsPanel.removeAll();
        for(String result : usernamesList){
            if(result.toLowerCase().contains(searchTerm.toLowerCase())){
                filteredResults.add(result);
            }
        }

        if(filteredResults.size() > 0){
            int colorCounter=0;
            for(String username : filteredResults){
                JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5,1)){};
                usernamePanel.setSize(505,30);
                if(colorCounter%2 ==0){
                    usernamePanel.setBackground(new Color(100,255,180));
                } else {
                    usernamePanel.setBackground(new Color(100,255,200));
                }
                JLabel usernameLabel = new JLabel(username);
                usernamePanel.add(usernameLabel);
                resultsPanel.add(usernamePanel);
                colorCounter ++;

            }
        }
        resultsPanel.revalidate();
        resultsPanel.repaint();
        }

}
