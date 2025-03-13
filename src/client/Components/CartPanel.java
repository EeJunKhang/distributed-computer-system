/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client.Components;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;



public class CartPanel extends JScrollPane implements QuantityChangeListener{
    private final JPanel cardsContainer;
    private final Map<String, CartCard2> itemMap;

    public CartPanel() {
        itemMap = new HashMap<>();
        cardsContainer = new JPanel();
        cardsContainer.setLayout(new BoxLayout(cardsContainer, BoxLayout.Y_AXIS));
        cardsContainer.setBackground(new Color(245, 245, 245));
        cardsContainer.add(Box.createVerticalGlue());

        setViewportView(cardsContainer);
        setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        setPreferredSize(new Dimension(200, Integer.MAX_VALUE));
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
    }
}
