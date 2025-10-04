package org.example.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import org.example.Main;
import org.example.Types;

public class SearchPanel extends JPanel {
    private final Main mainApp;
    private int pageNo = 0;
    private final int itemCount = 20;
    private List<Types.Item> items;
    private JTextField searchField;
    private JPanel itemsPanel;
    private JLabel pageLabel;

    public SearchPanel(Main mainApp) {
        this.mainApp = mainApp;
        setLayout(new BorderLayout(10, 10));

        JPanel searchBar = new JPanel(new BorderLayout(5, 5));
        searchField = new JTextField();
        JButton searchButton = new JButton("Search");
        searchBar.add(searchField, BorderLayout.CENTER);
        searchBar.add(searchButton, BorderLayout.EAST);
        add(searchBar, BorderLayout.NORTH);

        itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        add(scrollPane, BorderLayout.CENTER);

        JPanel pagination = new JPanel(new FlowLayout());
        JButton prevButton = new JButton("Prev");
        JButton nextButton = new JButton("Next");
        pageLabel = new JLabel("1");
        pagination.add(prevButton);
        pagination.add(pageLabel);
        pagination.add(nextButton);
        add(pagination, BorderLayout.SOUTH);

        JPanel bottomNav = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        JButton homeButton = new JButton("🏠 Home");
        bottomNav.add(homeButton);
        bottomNav.add(new JButton("🔍 Search"));
        add(bottomNav, BorderLayout.SOUTH);
        
        homeButton.addActionListener(_ -> mainApp.showScreen("home"));
        searchButton.addActionListener(_ -> refreshItems());
        prevButton.addActionListener(_ -> {
            if (pageNo > 0) pageNo--;
            refreshItems();
        });
        nextButton.addActionListener(_ -> {
            pageNo++;
            refreshItems();
        });
    }

    private void refreshItems() {
        String keyword = searchField.getText().trim();
        itemsPanel.removeAll();

        if (keyword.isEmpty()) {
            itemsPanel.add(new JLabel("Enter a keyword to search."));
        } else {
            items = mainApp.db.searchItem(keyword, pageNo * itemCount, itemCount, null);

            for (Types.Item item : items) {
                JPanel itemPanel = new JPanel();
                itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
                itemPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

                JLabel nameLabel = new JLabel(item.name(), SwingConstants.CENTER);
                nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel priceLabel = new JLabel("₹" + item.price(), SwingConstants.CENTER);
                priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel imageLabel = new JLabel();
                try {
                    byte[] imgData = mainApp.db.getFile(mainApp.db.getFileIds(item.id())[0]);
                    BufferedImage img = ImageIO.read(new ByteArrayInputStream(imgData));
                    imageLabel.setIcon(new ImageIcon(img.getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
                } catch (IOException ex) {
                    imageLabel.setText("[Image not available]");
                }

                imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                itemPanel.add(Box.createVerticalStrut(10));
                itemPanel.add(imageLabel);
                itemPanel.add(nameLabel);
                itemPanel.add(priceLabel);
                itemPanel.add(Box.createVerticalStrut(10));

                itemPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        ItemDetailPanel detail = new ItemDetailPanel(mainApp, item);
                        mainApp.addPanel(detail, "itemDetail");
                        mainApp.showScreen("itemDetail");
                    }
                });

                itemsPanel.add(itemPanel);
                itemsPanel.add(Box.createVerticalStrut(10));
            }
        }

        pageLabel.setText(String.valueOf(pageNo + 1));
        itemsPanel.revalidate();
        itemsPanel.repaint();
    }
}
