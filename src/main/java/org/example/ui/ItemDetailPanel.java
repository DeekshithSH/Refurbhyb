package org.example.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.example.Main;
import org.example.Types;

public class ItemDetailPanel extends JPanel {

    public ItemDetailPanel(Main mainApp, Types.Item item) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(new JLabel(item.name(), SwingConstants.CENTER), BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        // Image
        try {
            byte[] imgData = mainApp.db.getFile(mainApp.db.getFileIds(item.id())[0]);
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(imgData));
            JLabel imgLabel = new JLabel(new ImageIcon(img.getScaledInstance(-1, -1, Image.SCALE_SMOOTH)));
            imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            center.add(imgLabel);
        } catch (IOException e) {
            JLabel imgLabel = new JLabel(new ImageIcon(new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB)));
            imgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            center.add(imgLabel);
        }

        center.add(Box.createVerticalStrut(10));
        center.add(new JLabel("Price: ₹" + item.price(), SwingConstants.CENTER));
        center.add(Box.createVerticalStrut(15));
        center.add(new JLabel("<html><b>Description:</b> " + item.description() + "</html>"));
        center.add(Box.createVerticalStrut(5));
        center.add(new JLabel("<html><b>Seller:</b> " + item.seller() + "</html>"));

        add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        JButton backBtn = new JButton("⬅ Back");
        backBtn.addActionListener(_ -> mainApp.showScreen("search"));
        JButton buyBtn = new JButton("🛒 Buy Now");
        buyBtn.addActionListener(_ -> {
            BuyPanel buyPanel = new BuyPanel(mainApp, item);
            mainApp.addPanel(buyPanel, "buyPanel");
            mainApp.showScreen("buyPanel");
        });

        bottom.add(backBtn);
        bottom.add(buyBtn);
        add(bottom, BorderLayout.SOUTH);
    }
}
