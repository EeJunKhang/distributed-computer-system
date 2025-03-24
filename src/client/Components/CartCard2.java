package client.Components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import model.Items;

public class CartCard2 extends JPanel {
    
    private int quantity = 1;
    private BufferedImage image;
    private Items item;
    private JLabel quantityLabel;
    private QuantityChangeListener listener;
    
    public CartCard2(Items item) {
        try {
            this.image = ImageIO.read(new File(item.getImage()));
        } catch (IOException ex) {
            System.out.println("error");
        }
        this.item = item;
        setLayout(new BorderLayout(10, 0));
        setOpaque(false);
        createUI();
    }

    private void createUI() {
        // Image panel (left side)
        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (image != null) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                    double scale = Math.max(
                            (double) getWidth() / image.getWidth(),
                            (double) getHeight() / image.getHeight()
                    );
                    int scaledWidth = (int) (image.getWidth() * scale);
                    int scaledHeight = (int) (image.getHeight() * scale);

                    int x = (getWidth() - scaledWidth) / 2;
                    int y = (getHeight() - scaledHeight) / 2;

                    g2d.drawImage(image, x, y, scaledWidth, scaledHeight, null);
                    g2d.dispose();
                }
            }
        };
        imagePanel.setPreferredSize(new Dimension(80, 80)); // Smaller image area
        imagePanel.setBackground(new Color(240, 240, 240));

        // Right side content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(8, 0, 8, 8));

        // Title with constrained width
        JLabel titleLabel = new JLabel(item.getItemName());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleLabel.setMaximumSize(new Dimension(90, 20));

        // Subtitle
        JLabel subtitleLabel = new JLabel(item.getItemDescription());
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        subtitleLabel.setForeground(new Color(100, 100, 100));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitleLabel.setMaximumSize(new Dimension(90, 20));

        // Price
        JLabel priceLabel = new JLabel(String.valueOf(item.getPrice()));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 12));
        priceLabel.setForeground(new Color(40, 40, 40));
        priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Compact quantity selector
        JPanel quantityPanel = new JPanel();
        quantityPanel.setOpaque(false);
        quantityPanel.setLayout(new BoxLayout(quantityPanel, BoxLayout.X_AXIS));
        quantityPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        quantityPanel.setBorder(new EmptyBorder(6, 0, 0, 0));

        JButton minusButton = new JButton("-");
        styleButton(minusButton, 10);
        JButton plusButton = new JButton("+");
        styleButton(plusButton, 10);
        quantityLabel = new JLabel(String.valueOf(quantity));
        quantityLabel.setFont(new Font("Arial", Font.BOLD, 12));
        quantityLabel.setBorder(new EmptyBorder(0, 4, 0, 4));

        minusButton.addActionListener(e -> incrementQuantity(-1));

        plusButton.addActionListener(e -> incrementQuantity(1));

        quantityPanel.add(minusButton);
        quantityPanel.add(quantityLabel);
        quantityPanel.add(plusButton);

        // Add components to content panel
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(2));
        contentPanel.add(subtitleLabel);
        contentPanel.add(Box.createVerticalStrut(4));
        contentPanel.add(priceLabel);
        contentPanel.add(Box.createVerticalStrut(6));
        contentPanel.add(quantityPanel);

        add(imagePanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }

    public void incrementQuantity(int amount) {
        int newQuantity = quantity + amount;
        if (newQuantity < 0) return; // Prevent negative quantities
        quantity = newQuantity;
        quantityLabel.setText(String.valueOf(quantity));
        
        // Notify listener about quantity change
        if (listener != null) {
            listener.onQuantityChanged(this, quantity);
        }
    }

    private void styleButton(JButton button, int fontSize) {
        button.setFont(new Font("Arial", Font.BOLD, fontSize));
        button.setBackground(new Color(220, 220, 220));
        button.setForeground(Color.DARK_GRAY);
        button.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
        button.setFocusPainted(false);
        button.setMargin(new Insets(0, 0, 0, 0));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // Shadow
        g2d.setColor(new Color(0, 0, 0, 20));
        g2d.fillRoundRect(2, 3, getWidth() - 4, getHeight() - 4, 8, 8);

        // Background
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 6, 6);

        g2d.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(180, 100); // Fixed 180px width
    }
    
    public Items getItem() {
        return item;
    }
    
    public int getQuantity(){
        return quantity;
    }
    
    public void setQuantityChangeListener(QuantityChangeListener listener) {
        this.listener = listener;
    }

}
