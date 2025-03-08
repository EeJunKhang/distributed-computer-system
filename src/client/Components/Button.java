/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client.Components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 *
 * @author ejunk
 */
public class Button extends JButton {
    
    private Color normalBackgroundColor = new Color(66, 133, 244);
    private Color hoverBackgroundColor = new Color(77, 144, 254);
    private Color pressedBackgroundColor = new Color(55, 120, 229);
    private Color textColor = Color.WHITE;
    private boolean isHovered = false;
    private boolean isPressed = false;
    private int cornerRadius = 10;
    
    /**
     * Interface for button action listeners
     */
    public interface ButtonActionListener {
        void onAction();
    }
    
    /**
     * Default constructor
     */
    public Button() {
        this("");
    }
    
    /**
     * Constructor with button text
     * @param text the button text
     */
    public Button(String text) {
        super(text);
        setupButton();
    }
    
    /**
     * Constructor with button text and icon
     * @param text the button text
     * @param icon the button icon
     */
    public Button(String text, Icon icon) {
        super(text, icon);
        setupButton();
    }
    
    private void setupButton() {
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setForeground(textColor);
        setFont(new Font("Arial", Font.BOLD, 14));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Set preferred size for a nicer button
        setPreferredSize(new Dimension(120, 40));
        
        // Add mouse listeners for hover and press effects
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                isPressed = true;
                repaint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                isPressed = false;
                repaint();
                
            }
        });
    }
    
    /**
     * Set the corner radius for the button
     * @param radius the radius in pixels
     */
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }
    
    /**
     * Set custom colors for different button states
     * @param normalColor the normal background color
     * @param hoverColor the background color when hovered
     * @param pressedColor the background color when pressed
     * @param textColor the text color
     */
    public void setCustomColors(Color normalColor, Color hoverColor, Color pressedColor, Color textColor) {
        this.normalBackgroundColor = normalColor;
        this.hoverBackgroundColor = hoverColor;
        this.pressedBackgroundColor = pressedColor;
        this.textColor = textColor;
        setForeground(textColor);
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Determine the background color based on button state
        Color backgroundColor;
        
        if (isPressed) {
            backgroundColor = pressedBackgroundColor;
        } else if (isHovered) {
            backgroundColor = hoverBackgroundColor;
        } else {
            backgroundColor = normalBackgroundColor;
        }
        
        // Draw the rounded rectangle background
        g2d.setColor(backgroundColor);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        
        // Draw a subtle shadow effect when not pressed
        if (!isPressed) {
            g2d.setColor(new Color(0, 0, 0, 20));
            g2d.fillRoundRect(0, getHeight() - 2, getWidth(), 2, cornerRadius, cornerRadius);
        }
        
        // Let the UI delegate paint the text and icon
        super.paintComponent(g2d);
        
        g2d.dispose();
    }
}