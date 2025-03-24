/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client.Components;

import client.HomePage;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import model.Items;

public class FoodItemSection extends JPanel {

    private int quantity = 1;
    private JLabel quantityLabel;
    private JLabel imageLabel;
    private JPanel detailsPanel;
    private JLabel nameLabel;
    private JLabel descriptionLabel;
    private JLabel priceLabel;
    private JPanel controlsPanel;
    private JPanel quantityPanel;
    private JButton addToCartButton;
    private Items currentItem;

    public FoodItemSection() {
        initializeUI();
        showPlaceholders();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Image placeholder
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(300, 200));
        add(imageLabel, BorderLayout.NORTH);

        // Details panel
        detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));

        // Name placeholder
        nameLabel = new JLabel();
        nameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Description placeholder
        descriptionLabel = new JLabel();
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Price placeholder
        priceLabel = new JLabel();
        priceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        priceLabel.setForeground(Color.RED);
        priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        detailsPanel.add(nameLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        detailsPanel.add(descriptionLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        detailsPanel.add(priceLabel);

        add(detailsPanel, BorderLayout.CENTER);

        // Quantity controls
        quantityPanel = new JPanel();
        quantityPanel.setLayout(new BoxLayout(quantityPanel, BoxLayout.X_AXIS));

        JButton minusButton = new JButton("-");
        JButton plusButton = new JButton("+");
        quantityLabel = new JLabel("1");
        quantityLabel.setFont(new Font("Arial", Font.BOLD, 16));

        minusButton.addActionListener(e -> {
            if (quantity > 1) {
                quantity--;
                quantityLabel.setText(Integer.toString(quantity));
            }
        });

        plusButton.addActionListener(e -> {
            quantity++;
            quantityLabel.setText(Integer.toString(quantity));
        });

        quantityPanel.add(minusButton);
        quantityPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        quantityPanel.add(quantityLabel);
        quantityPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        quantityPanel.add(plusButton);

        // Add to Cart button
        addToCartButton = new JButton("Add to Cart");
        addToCartButton.setFont(new Font("Arial", Font.BOLD, 16));
        addToCartButton.setBackground(new Color(46, 204, 113));
        addToCartButton.setForeground(Color.WHITE);
        addToCartButton.setEnabled(false); // Disabled until item is loaded

        // Control panel
        controlsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controlsPanel.add(quantityPanel);
        controlsPanel.add(addToCartButton);
        add(controlsPanel, BorderLayout.SOUTH);
    }

    private void showPlaceholders() {
        // Image placeholder
        ImageIcon placeholderImage = createPlaceholderImage(300, 200);
        imageLabel.setIcon(placeholderImage);

        // Text placeholders
        nameLabel.setText("Select an Item");
        descriptionLabel.setText("Please choose a food item from the menu");
        priceLabel.setText("$0.00");

        // Grey out text
        nameLabel.setForeground(Color.GRAY);
        descriptionLabel.setForeground(Color.GRAY);
    }

    private ImageIcon createPlaceholderImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Draw background
        g2d.setColor(new Color(240, 240, 240));
        g2d.fillRect(0, 0, width, height);

        // Draw text
        g2d.setColor(Color.GRAY);
        g2d.setFont(new Font("Arial", Font.ITALIC, 16));
        String text = "Image Not Available";
        FontMetrics fm = g2d.getFontMetrics();
        int x = (width - fm.stringWidth(text)) / 2;
        int y = (height - fm.getHeight()) / 2 + fm.getAscent();
        g2d.drawString(text, x, y);

        g2d.dispose();
        return new ImageIcon(image);
    }

    public void loadItemData(Items item) {
        this.currentItem = item;
        addToCartButton.setEnabled(true);
        quantity = 1;
        quantityLabel.setText("1");

        // Load image
        try {
            ImageIcon foodImage = new ImageIcon(item.getImage());
            Image scaled = foodImage.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaled));
        } catch (Exception ex) {
            imageLabel.setIcon(createPlaceholderImage(300, 200));
            System.out.println("Error loading image: " + ex.getMessage());
        }

        // Set text
        nameLabel.setText(item.getItemName());
        descriptionLabel.setText(item.getItemDescription());
        priceLabel.setText(String.format("$%.2f", item.getPrice()));

        // Reset colors
        nameLabel.setForeground(Color.BLACK);
        descriptionLabel.setForeground(Color.BLACK);

        // Update cart button action
        if(addToCartButton.getActionListeners().length > 0){
            
            addToCartButton.removeActionListener(addToCartButton.getActionListeners()[0]);
        }
        addToCartButton.addActionListener(e -> {
            Items cartItem = new Items(
                    currentItem.getId(),
                    currentItem.getItemName(),
                    currentItem.getItemDescription(),
                    currentItem.getPrice(),
                    currentItem.getCategory(),
                    currentItem.getImage(),
                    currentItem.getStockQuantity(),
                    currentItem.getLastUpdated()
            );
//            cartItem.setQuantity(quantity);

            CartCard2 newCard = new CartCard2(cartItem);
            addToCartButton.setEnabled(false);
            EventBus.submitTask(() -> {
                try {
                    Thread.sleep(1000);
                    SwingUtilities.invokeLater(() -> {
                        HomePage.cartPanel.addCartItemAsync(newCard);
                        addToCartButton.setEnabled(true);
                    });
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            });
        });

        revalidate();
        repaint();
    }
}
