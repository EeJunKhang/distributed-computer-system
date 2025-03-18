/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client.Components;

import client.HomePage;
import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

public class CartPanel extends JScrollPane implements QuantityChangeListener {

    private final JPanel cardsContainer;
    private final Map<String, CartCard2> itemMap;
    private final LoadingSpinner spinner;
    private final JPanel loadingPanel;

    public CartPanel() {
        itemMap = new HashMap<>();

        // Create main container with OverlayLayout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new OverlayLayout(mainPanel));

        // Cards container setup
        cardsContainer = new JPanel();
        cardsContainer.setLayout(new BoxLayout(cardsContainer, BoxLayout.Y_AXIS));
        cardsContainer.setBackground(new Color(245, 245, 245));
        cardsContainer.add(Box.createVerticalGlue());

        // Loading panel setup
        loadingPanel = new JPanel(new GridBagLayout());
        loadingPanel.setOpaque(false);
        loadingPanel.setVisible(false);
        spinner = new LoadingSpinner();
        loadingPanel.add(spinner);

        // Add components to main panel
        mainPanel.add(cardsContainer);
        mainPanel.add(loadingPanel);

        // Configure scroll pane
        setViewportView(mainPanel);
        setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        setPreferredSize(new Dimension(200, Integer.MAX_VALUE));
    }

    public void addCartItemAsync(CartCard2 item) {
        showLoading();

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Simulate network/processing delay
                Thread.sleep(1500);
                return null;
            }

            @Override
            protected void done() {
                hideLoading();
                addCartItem(item);

            }
        };

        EventBus.submitTask(worker);
    }

    private void updateTotalPrice() {
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (var item : itemMap.entrySet()) {
            var card = item.getValue();
            var count = card.getQuantity();
            BigDecimal itemPrice = BigDecimal.valueOf(card.getItem().getPrice());

            totalPrice = totalPrice.add(itemPrice.multiply(BigDecimal.valueOf(count)));
        }

        totalPrice = totalPrice.setScale(2, RoundingMode.HALF_UP); // Keep 2 decimal places
        System.out.println(totalPrice);
        HomePage.totalPriceCart.setText("RM "+totalPrice.toString());
    }

    public void addCartItem(CartCard2 item) {
        String id = String.valueOf(item.getItem().getId());
        if (itemMap.containsKey(id)) {
            // Update existing item quantity
            CartCard2 existingCard = itemMap.get(id);
            existingCard.incrementQuantity(1);
        } else {

            item.setQuantityChangeListener(this);
            itemMap.put(id, item);

            // Add to UI
            JPanel wrapper = new JPanel(new BorderLayout());
            wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, item.getPreferredSize().height));
            wrapper.add(item, BorderLayout.CENTER);

            if (cardsContainer.getComponentCount() > 0) {
                cardsContainer.add(Box.createVerticalStrut(10), cardsContainer.getComponentCount() - 1);
            }

            cardsContainer.add(wrapper, cardsContainer.getComponentCount() - 1);
        }

        cardsContainer.revalidate();
        cardsContainer.repaint();
        updateTotalPrice();
    }

    private void showLoading() {
        SwingUtilities.invokeLater(() -> {
            loadingPanel.setVisible(true);
            spinner.start();
            revalidate();
            repaint();
        });
    }

    private void hideLoading() {
        SwingUtilities.invokeLater(() -> {
            loadingPanel.setVisible(false);
            spinner.stop();
            revalidate();
            repaint();
        });
    }

    public void removeCartItem(String id) {
        if (itemMap.containsKey(id)) {
            CartCard2 card = itemMap.remove(id);
            // Remove from UI
            for (Component comp : cardsContainer.getComponents()) {
                if (comp instanceof JPanel wrapper && wrapper.getComponent(0) == card) {
                    cardsContainer.remove(wrapper);
                    break;
                }
            }
            cardsContainer.revalidate();
            cardsContainer.repaint();
        }
    }

    @Override
    public void onQuantityChanged(CartCard2 card, int newQuantity) {
        if (newQuantity <= 0) {
            // Remove item when quantity reaches 0
            String itemId = String.valueOf(card.getItem().getId());
            removeCartItem(itemId);
        }
        updateTotalPrice();
    }
}
