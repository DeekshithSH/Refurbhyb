package org.example.ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import org.example.Main;
import org.example.Types;

public class SellerOrdersPanel extends JPanel {
    public SellerOrdersPanel(Main mainApp, List<Types.OrderItem> orders) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("🛒 Seller Orders", SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        if (orders.isEmpty()) {
            JLabel emptyLabel = new JLabel("No orders yet.");
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            listPanel.add(emptyLabel);
        } else {
            for (Types.OrderItem order : orders) {
                JPanel orderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
                orderPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

                JLabel itemLabel = new JLabel("Item ID: " + order.item_id());
                JLabel tnId = new JLabel("Transaction Id: " + order.transaction_id());
                itemLabel.setPreferredSize(new Dimension(200, 25));

                JButton verifyBtn = new JButton(order.payment_success() ? "✅ Paid" : "💲 Verify Payment");
                verifyBtn.setEnabled(!order.payment_success());

                verifyBtn.addActionListener(_ -> {
                    JPanel popupPanel = new JPanel(new GridLayout(2, 2, 5, 5));
                    JTextField trackingField = new JTextField();
                    JTextField partnerField = new JTextField();
                    popupPanel.add(new JLabel("Tracking ID:"));
                    popupPanel.add(trackingField);
                    popupPanel.add(new JLabel("Shipping Partner:"));
                    popupPanel.add(partnerField);

                    int result = JOptionPane.showConfirmDialog(
                            this,
                            popupPanel,
                            "Add Shipping Details",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE
                    );

                    if (result == JOptionPane.OK_OPTION) {
                        String trackingId = trackingField.getText().trim();
                        String partner = partnerField.getText().trim();
                        if (!trackingId.isEmpty() && !partner.isEmpty()) {
                            mainApp.db.verifyPaymentAndShip(order.id(), trackingId, partner);
                            verifyBtn.setText("✅ Paid & Shipped");
                            verifyBtn.setEnabled(false);
                            JOptionPane.showMessageDialog(
                                    this,
                                    "Order updated successfully!",
                                    "Success",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        } else {
                            JOptionPane.showMessageDialog(
                                    this,
                                    "Please enter both Tracking ID and Partner.",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                });

                orderPanel.add(itemLabel);
                orderPanel.add(tnId);
                orderPanel.add(verifyBtn);

                listPanel.add(orderPanel);
            }
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        add(scrollPane, BorderLayout.CENTER);

        JButton backBtn = new JButton("⬅ Back");
        backBtn.addActionListener(_ -> mainApp.showScreen("home"));
        add(backBtn, BorderLayout.SOUTH);
    }
}
