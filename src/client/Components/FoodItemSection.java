/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client.Components;

import client.HomePage;
import static java.awt.AWTEventMulticaster.add;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import model.Item;

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

    public FoodItemSection() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Food Image
        ImageIcon foodImage = new ImageIcon("src/resources/burger.jpg"); // Replace with your image path
        imageLabel = new JLabel(new ImageIcon(foodImage.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH)));
        add(imageLabel, BorderLayout.NORTH);

        // Food Details Panel
        detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));

        // Food Name
        nameLabel = new JLabel("Deluxe Burger");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Food Description
        descriptionLabel = new JLabel("Juicy beef patty with melted cheese, fresh lettuce, tomatoes, and our special sauce.");
//        descriptionLabel.setLineWrap(true);
//        descriptionLabel.setWrapStyleWord(true);
//        descriptionLabel.setEditable(false);
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Food Price
        priceLabel = new JLabel("$12.99");
        priceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        priceLabel.setForeground(Color.RED);
        priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        detailsPanel.add(nameLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        detailsPanel.add(descriptionLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        detailsPanel.add(priceLabel);

        add(detailsPanel, BorderLayout.CENTER);

        // Quantity Controls and Add to Cart Button
        controlsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // Quantity Counter
        quantityPanel = new JPanel();
        quantityPanel.setLayout(new BoxLayout(quantityPanel, BoxLayout.X_AXIS));

        JButton minusButton = new JButton("-");
        JButton plusButton = new JButton("+");
        quantityLabel = new JLabel("1");
        quantityLabel.setFont(new Font("Arial", Font.BOLD, 16));

        minusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (quantity > 1) {
                    quantity--;
                    quantityLabel.setText(Integer.toString(quantity));
                }
            }
        });

        plusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                quantity++;
                quantityLabel.setText(Integer.toString(quantity));
            }
        });

        quantityPanel.add(minusButton);
        quantityPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        quantityPanel.add(quantityLabel);
        quantityPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        quantityPanel.add(plusButton);

        // Add to Cart Button
        addToCartButton = new JButton("Add to Cart");
        addToCartButton.setFont(new Font("Arial", Font.BOLD, 16));
        addToCartButton.setBackground(new Color(46, 204, 113));
        addToCartButton.setForeground(Color.WHITE);
        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                JOptionPane.showMessageDialog(null, "Added " + quantity + " item(s) to cart!");
                Item item = new Item(1,"name","sd",4, "src/resources/burger.jpg");
                CartCard2 newCard = new CartCard2(item);
                HomePage.cartPanel.addCartItem(newCard);
                // Here you would typically add the item to a shopping cart
            }
        });

        controlsPanel.add(quantityPanel);
        controlsPanel.add(addToCartButton);
        add(controlsPanel, BorderLayout.SOUTH);
    }
}
