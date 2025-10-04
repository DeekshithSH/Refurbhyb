package org.example.ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import org.example.Main;
import org.example.Types;

public class SellerMyItemsPanel extends JPanel {

    public SellerMyItemsPanel(Main mainApp, List<Types.Item> items) {

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("📦 My Items", SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        if (items.isEmpty()) {
            JLabel emptyLabel = new JLabel("No items available.");
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            listPanel.add(emptyLabel);
        } else {
            for (Types.Item item : items) {
                JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
                itemPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

                JLabel itemLabel = new JLabel(item.name() + " | ₹" + item.price());
                itemLabel.setPreferredSize(new Dimension(300, 25));

                JButton removeBtn = new JButton("❌ Remove");
                removeBtn.addActionListener(_ -> {
                    int confirm = JOptionPane.showConfirmDialog(
                            this,
                            "Are you sure you want to remove: " + item.name() + "?",
                            "Confirm Remove",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        mainApp.db.removeItem(item.id());

                        listPanel.remove(itemPanel);
                        listPanel.revalidate();
                        listPanel.repaint();

                        JOptionPane.showMessageDialog(this, "Item removed successfully!", "Removed", JOptionPane.INFORMATION_MESSAGE);
                    }
                });

                itemPanel.add(itemLabel);
                itemPanel.add(removeBtn);

                listPanel.add(itemPanel);
                listPanel.add(Box.createVerticalStrut(5));
            }
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        add(scrollPane, BorderLayout.CENTER);

        JButton backBtn = new JButton("⬅ Back");
        backBtn.addActionListener(_ -> mainApp.showScreen("home"));
        add(backBtn, BorderLayout.SOUTH);
    }
}
