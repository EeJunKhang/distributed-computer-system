package client.Components;

import client.Interface.HomePage;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import model.Products;

public class FoodItemPanel extends JPanel {

    public FoodItemPanel(Products item, FoodItemSection page) {
        setLayout(new BorderLayout(5, 5));
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setBackground(Color.WHITE);

        // Image panel (placeholder for food image)
        JPanel imagePanel = new ImagePanel(item.getImage(), 20);
        imagePanel.setPreferredSize(new Dimension(180, 120));

        imagePanel.setLayout(new BorderLayout());

        // Info panel
        JPanel infoPanel = new JPanel(new BorderLayout(5, 0));
        infoPanel.setOpaque(false);

        // Name label
        JLabel nameLabel = new JLabel(item.getItemName());
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        // Price label
        JLabel priceLabel = new JLabel("$" + item.getPrice());
        priceLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        priceLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        // description label
        String text = (item.getItemDescription().length() > 20) ? item.getItemDescription().substring(0, 17) + "..." : item.getItemDescription();
        JLabel restaurantLabel = new JLabel(text);
        restaurantLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        restaurantLabel.setForeground(new Color(120, 120, 120));

        // Add components to info panel
        JPanel topInfoPanel = new JPanel(new BorderLayout());
        topInfoPanel.setOpaque(false);
        topInfoPanel.add(nameLabel, BorderLayout.WEST);
        topInfoPanel.add(priceLabel, BorderLayout.EAST);

        infoPanel.add(topInfoPanel, BorderLayout.NORTH);
        infoPanel.add(restaurantLabel, BorderLayout.SOUTH);

        // Add components to item panel
        add(imagePanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);

        // Hover effect
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBorder(new CompoundBorder(
                        new LineBorder(new Color(230, 230, 230), 1),
                        new EmptyBorder(4, 4, 4, 4)
                ));
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); 
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBorder(new EmptyBorder(5, 5, 5, 5));
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)); 
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                page.loadItemData(item);
                HomePage.mainTab.setSelectedIndex(1);
            }
        });

    }
}
