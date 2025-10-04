package org.example.ui;

import javax.swing.*;
import java.awt.*;
import org.example.Main;

public class HomePanel extends JPanel {
    private final Main mainApp;

    public HomePanel(Main mainApp) {
        this.mainApp = mainApp;
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton myOrdersButton = new JButton("My Orders");
        JButton logoutButton = new JButton("Logout");
        topPanel.add(myOrdersButton);
        topPanel.add(logoutButton);
        add(topPanel, BorderLayout.NORTH);

        JPanel homeView = createHomeView();

        add(homeView, BorderLayout.CENTER);

        JPanel bottomNav = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        JButton homeButton = new JButton("🏠 Home");
        JButton searchButton = new JButton("🔍 Search");
        bottomNav.add(homeButton);
        bottomNav.add(searchButton);
        add(bottomNav, BorderLayout.SOUTH);

        SearchPanel searchPanel = new SearchPanel(mainApp);
        mainApp.addPanel(searchPanel, "search");

        searchButton.addActionListener(_ -> mainApp.showScreen("search"));

        logoutButton.addActionListener(_ -> {
            mainApp.user = null;
            mainApp.showScreen("welcome");
        });

        myOrdersButton.addActionListener(_ -> {
            MyOrdersPanel myOrders = new MyOrdersPanel(mainApp);
            mainApp.addPanel(myOrders, "MyOrder");
            mainApp.showScreen("MyOrder");
        });
    }

    private JPanel createHomeView() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel welcomeLabel = new JLabel("Welcome, " + mainApp.user.name(), SwingConstants.CENTER);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalStrut(40));
        panel.add(welcomeLabel);
        panel.add(Box.createVerticalGlue());

        return panel;
    }
}
