package client.Components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import utils.ImagePathGetter;

public class ImagePanel extends JPanel {

    private BufferedImage image;
    private int borderRadius = 20; // Default border radius

    public ImagePanel(String imagePath) {
        this(imagePath, 20); // Default constructor with default border radius
    }

    public ImagePanel(String imagePath, int borderRadius) {
        setOpaque(false); // Make panel transparent
        this.borderRadius = borderRadius;
//        InputStream is = getClass().getClassLoader().getResourceAsStream(imagePath);
//        if (is != null) {
//            try {
//                image = ImageIO.read(is);
//            } catch (IOException ex) {
//                System.err.println("Error loading image: " + ex.getMessage());
//                setPreferredSize(new Dimension(300, 200)); // Default size if image fails to load
//            }
//            setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
//        } else {
//            System.err.println("Image not found in classpath");
//            setPreferredSize(new Dimension(300, 200));
//        }
        try {
            image = ImageIO.read(new File(ImagePathGetter.getImageFullPath(imagePath))); // Load image from file
            setPreferredSize(new Dimension(image.getWidth(), image.getHeight())); // Set panel size to image size
        } catch (IOException e) {
            System.err.println("Error loading image: " + e.getMessage());
            setPreferredSize(new Dimension(300, 200)); // Default size if image fails to load
        }
    }

    /**
     * Set the border radius for rounded corners
     *
     * @param borderRadius the radius of the rounded corners in pixels
     */
    public void setBorderRadius(int borderRadius) {
        this.borderRadius = borderRadius;
        repaint(); // Repaint the panel with new border radius
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (image != null) {
            Graphics2D g2d = (Graphics2D) g.create();

            // Enable anti-aliasing for smoother corners
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Create a rounded rectangle shape for clipping
            RoundRectangle2D roundedRect = new RoundRectangle2D.Float(
                    0, 0, getWidth(), getHeight(), borderRadius, borderRadius);

            // Set the clip to the rounded rectangle
            g2d.setClip(roundedRect);

            // Draw the image
            g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);

            // Clean up
            g2d.dispose();
        }
    }
}
