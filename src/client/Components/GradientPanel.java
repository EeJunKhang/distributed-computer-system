/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client.Components;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/**
 * A custom JPanel that paints its background with a gradient color.
 * The gradient colors can be set by the user.
 */
public class GradientPanel extends JPanel {
    
    private Color color1;
    private Color color2;
    private GradientDirection direction;
    
    /**
     * Enum to specify the direction of the gradient
     */
    public enum GradientDirection {
        TOP_TO_BOTTOM,
        LEFT_TO_RIGHT,
        DIAGONAL_TOP_LEFT_TO_BOTTOM_RIGHT,
        DIAGONAL_TOP_RIGHT_TO_BOTTOM_LEFT
    }
    
    /**
     * Default constructor with preset colors and direction
     */
    public GradientPanel() {
        this(Color.WHITE, Color.BLUE, GradientDirection.TOP_TO_BOTTOM);
    }
    
    /**
     * Constructor allowing specification of both colors and direction
     * 
     * @param color1 First color in the gradient
     * @param color2 Second color in the gradient
     * @param direction Direction of the gradient
     */
    public GradientPanel(Color color1, Color color2, GradientDirection direction) {
        this.color1 = color1;
        this.color2 = color2;
        this.direction = direction;
        setOpaque(false); // Important for proper gradient rendering
    }
    
    /**
     * Set the first color of the gradient
     * 
     * @param color The new first color
     */
    public void setColor1(Color color) {
        this.color1 = color;
        repaint();
    }
    
    /**
     * Set the second color of the gradient
     * 
     * @param color The new second color
     */
    public void setColor2(Color color) {
        this.color2 = color;
        repaint();
    }
    
    /**
     * Set both colors of the gradient
     * 
     * @param color1 The new first color
     * @param color2 The new second color
     */
    public void setColors(Color color1, Color color2) {
        this.color1 = color1;
        this.color2 = color2;
        repaint();
    }
    
    /**
     * Set the direction of the gradient
     * 
     * @param direction The new gradient direction
     */
    public void setDirection(GradientDirection direction) {
        this.direction = direction;
        repaint();
    }
    
    /**
     * Get the first color of the gradient
     * 
     * @return The first color
     */
    public Color getColor1() {
        return color1;
    }
    
    /**
     * Get the second color of the gradient
     * 
     * @return The second color
     */
    public Color getColor2() {
        return color2;
    }
    
    /**
     * Get the gradient direction
     * 
     * @return The current gradient direction
     */
    public GradientDirection getDirection() {
        return direction;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Cast to Graphics2D for advanced rendering
        Graphics2D g2d = (Graphics2D) g.create();
        
        // Set rendering hints for better quality
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int w = getWidth();
        int h = getHeight();
        
        // Create gradient paint based on direction
        GradientPaint gp = null;
        
        switch (direction) {
            case TOP_TO_BOTTOM:
                gp = new GradientPaint(0, 0, color1, 0, h, color2);
                break;
                
            case LEFT_TO_RIGHT:
                gp = new GradientPaint(0, 0, color1, w, 0, color2);
                break;
                
            case DIAGONAL_TOP_LEFT_TO_BOTTOM_RIGHT:
                gp = new GradientPaint(0, 0, color1, w, h, color2);
                break;
                
            case DIAGONAL_TOP_RIGHT_TO_BOTTOM_LEFT:
                gp = new GradientPaint(w, 0, color1, 0, h, color2);
                break;
        }
        
        // Set the gradient paint and fill rectangle
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
        
        g2d.dispose();
    }
}