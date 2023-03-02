package com.techelevator.tenmo.consoleGUI;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.services.ActiveService;
import com.techelevator.tenmo.services.TransferService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ConsoleGUI extends JFrame {
    private static final String API_BASE_URL = "http://localhost:8080/";
    private AuthenticatedUser currentUser;

    Image backgroundImage = Toolkit.getDefaultToolkit().getImage("C:\\Users\\Kenny\\Desktop\\meritamerica\\Module2\\module-2-capstone\\tenmo-client\\src\\main\\resources\\Images\\Backgrounds\\Untitled design (15).png");
    ImageIcon backgroundIcon = new ImageIcon((backgroundImage.getScaledInstance(560, 1000, Image.SCALE_SMOOTH)));
    JLabel backGroundLabel = new JLabel(backgroundIcon);

    //PANELS
    SearchBarPanel searchBarPanel;
    MainMenuPanel mainMenuPanel;

    JPanel navigationButtonPanel;

    JPanel cardPanel;

    private int mouseX;
    private int mouseY;
    java.util.List<String> usernames;

    final static String MAIN_MENU = "Main Menu";
    final static String SEARCH_MENU = "Search Menu";

    private final ActiveService activeService = new ActiveService(API_BASE_URL, currentUser);

    private final TransferService transferService = new TransferService(API_BASE_URL, currentUser);


    //BUTTONS

    //Image logo = Toolkit.getDefaultToolkit().getImage("C:\\Users\\Kenny\\Desktop\\meritamerica\\Module2\\module-2-capstone\\tenmo-client\\src\\main\\resources\\Images\\TenmoLogo.png");

    public ConsoleGUI(AuthenticatedUser currentUser) {
        super("TEnmo");


        this.currentUser= currentUser;
        activeService.setCurrentUser(currentUser);
        activeService.setAuthToken(currentUser.getToken());
        transferService.setCurrentUser(currentUser);
        transferService.setAuthToken(currentUser.getToken());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(560, 1000);
        setLocationRelativeTo(null);
        setResizable(false);
        setUndecorated(true);
        setLayout(null);
        setDefaultLookAndFeelDecorated(true);

        addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int deltaX = e.getX() - mouseX;
                int deltaY = e.getY() - mouseY;
                setLocation(getX() + deltaX, getY() + deltaY);
            }
        });

        setContentPane(backGroundLabel);

        usernames = activeService.getAllUsernames();

        cardPanel = new JPanel(new CardLayout());
        cardPanel.setBounds(0,150, 560,620);
        cardPanel.setOpaque(false);

        mainMenuPanel = new MainMenuPanel();
        mainMenuPanel.setLocation(0, 100);
        cardPanel.add(mainMenuPanel, MAIN_MENU);

        searchBarPanel = new SearchBarPanel(usernames);
        searchBarPanel.setBounds(10, 100, 540, 600);
        searchBarPanel.setBackground(new Color(10, 120, 120));
        searchBarPanel.setOpaque(false);
        cardPanel.add(searchBarPanel, SEARCH_MENU);


        add(cardPanel);

        CardLayout cardLayout = (CardLayout) cardPanel.getLayout();
        cardLayout.show(cardPanel, MAIN_MENU);

        //////NAVIGATION BUTTONS PANEL


        navigationButtonPanel = new JPanel() {};

        navigationButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        navigationButtonPanel.setSize(560, 145);
        navigationButtonPanel.setOpaque(false);

        JButton homeButton = new JButton(new ImageIcon("C:\\Users\\Kenny\\Desktop\\meritamerica\\Module2\\module-2-capstone\\tenmo-client\\src\\main\\resources\\Images\\icons8-menu-rounded-50.png"));
        homeButton.setPreferredSize(new Dimension(125, 125));
        navigationButtonPanel.add(homeButton);

        JButton accountButton = new JButton(new ImageIcon("C:\\Users\\Kenny\\Desktop\\meritamerica\\Module2\\module-2-capstone\\tenmo-client\\src\\main\\resources\\Images\\icons8-money-bag-50.png"));
        accountButton.setPreferredSize(new Dimension(125, 125));
        navigationButtonPanel.add(accountButton);

        JButton searchButton = new JButton(new ImageIcon("C:\\Users\\Kenny\\Desktop\\meritamerica\\Module2\\module-2-capstone\\tenmo-client\\src\\main\\resources\\Images\\icons8-search-50.png"));
        searchButton.setPreferredSize(new Dimension(125, 125));
        navigationButtonPanel.add(searchButton);
        searchButton.addActionListener(e -> {
        cardLayout.show(cardPanel ,SEARCH_MENU);
        });

        JButton contactsButton = new JButton(new ImageIcon("C:\\Users\\Kenny\\Desktop\\meritamerica\\Module2\\module-2-capstone\\tenmo-client\\src\\main\\resources\\Images\\icons8-address-book-50.png"));
        contactsButton.setPreferredSize(new Dimension(125, 125));
        navigationButtonPanel.add(contactsButton);


        //navigationButtonPanel.setLocation(0, 850);
        navigationButtonPanel.setBounds(0,850,560,200);
        add(navigationButtonPanel);


        //EXIT AND (MAYBE) MINIMIZE BUTTONS

        JButton exitButton = new JButton("X");
        exitButton.addActionListener(e -> System.exit(0));
        exitButton.setSize(50, 50);
        exitButton.setMargin(new Insets(0, 0, 0, 0));

        JPanel exitPanel = new JPanel(new GridLayout());
        exitPanel.add(exitButton);
        exitPanel.setLocation(535, 5);
        exitPanel.setSize(20, 20);
        add(exitPanel);

        setVisible(true);
    }

    public void changePanels(JPanel[] oldPanels, JPanel[] newPanels) {
        for (JPanel panel : oldPanels) {
            panel.setVisible(false);
        }
        for (JPanel panel : newPanels) {
            panel.setVisible(true);
        }

    }


}
