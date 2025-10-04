package org.example;

import javax.swing.*;
import java.awt.*;
import org.example.ui.*;
import org.example.Types.*;

public class Main {
    private JFrame frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    public Database db;
    public User user;

    public Main() {
        db = new Database("jdbc:mysql://localhost:3306/refurby?user=root&password=mysql");
        frame = new JFrame("Refurbhy");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        WelcomePanel welcomePanel = new WelcomePanel(this);
        LoginPanel loginPanel = new LoginPanel(this);
        SignUp signupPanel = new SignUp(this);

        mainPanel.add(welcomePanel, "welcome");
        mainPanel.add(loginPanel, "login");
        mainPanel.add(signupPanel, "signup");

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    public void showScreen(String name) {
        cardLayout.show(mainPanel, name);
    }

    public void addPanel(JPanel panel, String name) {
        mainPanel.add(panel, name);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}

class WelcomePanel extends JPanel {
    public WelcomePanel(Main mainApp) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel label = new JLabel("Welcome! Please log in or sign up.", SwingConstants.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        label.setBorder(BorderFactory.createEmptyBorder(40, 0, 30, 0));

        JButton loginButton = new JButton("Login");
        JButton signupButton = new JButton("Sign Up");

        Dimension buttonSize = new Dimension(120, 35);
        loginButton.setPreferredSize(buttonSize);
        signupButton.setPreferredSize(buttonSize);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.add(loginButton);
        buttonPanel.add(signupButton);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(label, BorderLayout.NORTH);
        centerPanel.add(buttonPanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        loginButton.addActionListener(_ -> mainApp.showScreen("login"));
        signupButton.addActionListener(_ -> mainApp.showScreen("signup"));
    }
}

