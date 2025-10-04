package org.example.ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import org.example.Main;
import org.example.Types;

public class SellerPanel extends JPanel {

    public SellerPanel(Main mainApp) {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Title
        add(new JLabel("📦 Seller Home", SwingConstants.CENTER), BorderLayout.NORTH);

        // Vertical button panel
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(0, 1, 0, 20)); // one column, variable rows, 20px spacing

        JButton ordersBtn = new JButton("🛒 Orders");
        JButton addItemBtn = new JButton("➕ Add Item");
        JButton myItemsBtn = new JButton("📦 My Items");
        JButton logoutBtn = new JButton("➜ Logout");

        buttons.add(ordersBtn);
        buttons.add(addItemBtn);
        buttons.add(myItemsBtn);
        buttons.add(logoutBtn);

        add(buttons, BorderLayout.CENTER);

        // Actions
        ordersBtn.addActionListener(_ -> {
            List<Types.OrderItem> sellerOrders = mainApp.db.getUserOrders(null, mainApp.user.uid());
            mainApp.addPanel(new SellerOrdersPanel(mainApp, sellerOrders), "sellerOrders");
            mainApp.showScreen("sellerOrders");
        });

        addItemBtn.addActionListener(_ -> {
            mainApp.addPanel(new SellerAddPanel(mainApp), "addItem");
            mainApp.showScreen("addItem");
        });

        myItemsBtn.addActionListener(_ -> {
            List<Types.Item> items = mainApp.db.getItems(mainApp.user.uid());
            mainApp.addPanel(new SellerMyItemsPanel(mainApp, items), "myItems");
            mainApp.showScreen("myItems");
        });

        logoutBtn.addActionListener(_ -> {
            mainApp.user = null;
            mainApp.showScreen("welcome");
        });
    }
}
