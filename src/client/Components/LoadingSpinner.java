package client.Components;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JComponent;

// Loading animation component
class LoadingSpinner extends JComponent {
    private float angle = 0f;
    private boolean running = false;

    public LoadingSpinner() {
        setPreferredSize(new Dimension(40, 40));
    }

    public void start() {
        running = true;
        new Thread(() -> {
            while (running) {
                angle += 0.2;
                repaint();
                try { Thread.sleep(20); } 
                catch (InterruptedException e) { /* Ignore */ }
            }
        }).start();
    }

    public void stop() {
        running = false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                           RenderingHints.VALUE_ANTIALIAS_ON);
        
        int size = Math.min(getWidth(), getHeight());
        g2d.setColor(new Color(46, 204, 113));
        g2d.setStroke(new BasicStroke(3f));
        g2d.drawArc((getWidth()-size)/2, (getHeight()-size)/2, 
                   size, size, 0, (int)(angle * 180));
        g2d.dispose();
    }
}
