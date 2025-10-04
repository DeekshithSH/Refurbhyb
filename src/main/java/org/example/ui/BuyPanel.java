package org.example.ui;

import javax.swing.*;
import java.awt.*;
import org.example.Main;
import org.example.Types;

public class BuyPanel extends JPanel {
    public BuyPanel(Main mainApp, Types.Item item) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Types.User seller = mainApp.db.getUserProfile(item.seller());

        add(new JLabel(item.name(), SwingConstants.CENTER), BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField txnField = new JTextField(20);
        txnField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        center.add(new JLabel("Price: ₹" + item.price()));
        center.add(Box.createVerticalStrut(10));
        center.add(new JLabel("Seller Email: " + seller.email()));
        center.add(new JLabel("Seller Phone: " + seller.phoneNo()));
        center.add(Box.createVerticalStrut(15));
        center.add(new JLabel("Enter Transaction ID:"));
        center.add(txnField);

        add(center, BorderLayout.CENTER);

        // Bottom buttons
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));

        JButton backBtn = new JButton("⬅ Back");
        backBtn.addActionListener(_ -> mainApp.showScreen("itemDetail"));

        JButton buyBtn = new JButton("✅ Confirm Buy");
        buyBtn.addActionListener(_ -> {
            String txnId = txnField.getText().trim();
            if (txnId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a valid Transaction ID.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            mainApp.db.placeOrder(mainApp.user.uid(), item.id(), 1, txnId);
            JOptionPane.showMessageDialog(this,
                    "Purchase confirmed!\nTransaction ID: " + txnId + "\nSeller: " + seller.email(),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            mainApp.showScreen("home");
        });

        bottom.add(backBtn);
        bottom.add(buyBtn);
        add(bottom, BorderLayout.SOUTH);
    }
}
