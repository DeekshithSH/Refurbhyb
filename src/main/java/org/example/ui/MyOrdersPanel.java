package org.example.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.example.Main;
import org.example.Types;

public class MyOrdersPanel extends JPanel {

    public MyOrdersPanel(Main mainApp) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("📦 My Orders", SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        List<Types.OrderItem> orders = mainApp.db.getUserOrders(mainApp.user.uid(), null);
        String[] columns = {"Item", "Payment Status", "Shipping Status", "Date"};

        DefaultTableModel model = new DefaultTableModel(columns, 0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm");

        for (Types.OrderItem order : orders) {
            Types.Item item = mainApp.db.getItem(order.item_id());

            String paymentStatus = order.payment_success() ? "✅ Accepted" : "❌ Not Verified";
            String shipStatus = order.shipped() ?
                    "Shipped ✔ | " + order.shipping_partner() + " (" + order.tracking_id() + ")" :
                    "Not Shipped ❌";
            String date = order.created_at().format(fmt);

            model.addRow(new Object[]{item.name(), paymentStatus, shipStatus, date});
        }

        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JButton backBtn = new JButton("⬅ Back");
        backBtn.addActionListener(_ -> mainApp.showScreen("home"));
        add(backBtn, BorderLayout.SOUTH);

        if (orders.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No orders yet.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
