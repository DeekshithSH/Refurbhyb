package org.example.ui;

import javax.swing.*;
import java.awt.*;
import org.example.Main;

public class SignUp extends JPanel {

    public SignUp(Main mainApp) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Form fields
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        JTextField emailField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField phoneField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JTextField addressField = new JTextField();

        formPanel.add(new JLabel("Email:"));        formPanel.add(emailField);
        formPanel.add(new JLabel("Name:"));         formPanel.add(nameField);
        formPanel.add(new JLabel("Phone Number:")); formPanel.add(phoneField);
        formPanel.add(new JLabel("Password:"));     formPanel.add(passField);
        formPanel.add(new JLabel("Address:"));      formPanel.add(addressField);

        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(formPanel, BorderLayout.CENTER);
        add(Box.createVerticalStrut(15));

        JPanel bottomNav = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        JButton backBtn = new JButton("⬅ Back");
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton signUpBtn = new JButton("Sign Up");
        signUpBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        bottomNav.add(backBtn);
        bottomNav.add(signUpBtn);
        add(bottomNav, BorderLayout.SOUTH);

        backBtn.addActionListener(_ -> mainApp.showScreen("welcome"));
        signUpBtn.addActionListener(_ -> {
            mainApp.db.createUser(
                    emailField.getText(),
                    nameField.getText(),
                    phoneField.getText(),
                    new String(passField.getPassword()),
                    addressField.getText()
            );
            mainApp.showScreen("login");
        });
    }
}
