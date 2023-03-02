package com.techelevator.tenmo.consoleGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NavigationButtonPanel extends JPanel {

    public NavigationButtonPanel(){

        setLayout(new FlowLayout(FlowLayout.CENTER, 10,10));
        setSize(560,145);
        setOpaque(false);

        JButton homeButton = new JButton(new ImageIcon("C:\\Users\\Kenny\\Desktop\\meritamerica\\Module2\\module-2-capstone\\tenmo-client\\src\\main\\resources\\Images\\icons8-menu-rounded-50.png"));
        homeButton.setPreferredSize(new Dimension(125 , 125));
        add(homeButton);

        JButton accountButton = new JButton(new ImageIcon("C:\\Users\\Kenny\\Desktop\\meritamerica\\Module2\\module-2-capstone\\tenmo-client\\src\\main\\resources\\Images\\icons8-money-bag-50.png"));
        accountButton.setPreferredSize(new Dimension(125 , 125));
        add(accountButton);

        JButton searchButton = new JButton(new ImageIcon("C:\\Users\\Kenny\\Desktop\\meritamerica\\Module2\\module-2-capstone\\tenmo-client\\src\\main\\resources\\Images\\icons8-search-50.png"));
        searchButton.setPreferredSize(new Dimension(125 , 125));
        add(searchButton);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        JButton contactsButton = new JButton(new ImageIcon("C:\\Users\\Kenny\\Desktop\\meritamerica\\Module2\\module-2-capstone\\tenmo-client\\src\\main\\resources\\Images\\icons8-address-book-50.png"));
        contactsButton.setPreferredSize(new Dimension(125 , 125));
        add(contactsButton);


    }

    public void changePanels(JPanel[] oldPanels, JPanel[] newPanels){
        for(JPanel panel : oldPanels){
            panel.setVisible(false);
        }
        for (JPanel panel : newPanels){
            panel.setVisible(true);
        }

    }

}
