package org.example.ui;

import javax.swing.*;
import java.awt.*;
import org.example.Main;
import org.example.Types.*;

public class LoginPanel extends JPanel {
    public LoginPanel(Main mainApp) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JLabel userLabel = new JLabel("Email:");
        JTextField userField = new JTextField(15);

        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(15);

        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Back");

        add(userLabel);
        add(userField);
        add(passLabel);
        add(passField);
        add(loginButton);
        add(backButton);

        backButton.addActionListener(_ -> mainApp.showScreen("welcome"));

        loginButton.addActionListener(_ -> {
            String email = userField.getText().trim();
            String password = new String(passField.getPassword());

            String uid = mainApp.db.validate(email, password);
            if (uid == null) {
                JOptionPane.showMessageDialog(this,
                        "Invalid username or password!",
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            User user = mainApp.db.getUserProfile(uid);
            mainApp.setUser(user);

            String[] options = {"Buyer", "Seller"};
            int choice = JOptionPane.showOptionDialog(
                    this,
                    "Login as:",
                    "Select Role",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            JPanel homePanel = (choice == 0)
                    ? new HomePanel(mainApp)
                    : new SellerPanel(mainApp);

            mainApp.addPanel(homePanel, "home");
            mainApp.showScreen("home");
        });
    }
}
