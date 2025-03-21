/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client.Components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSpinnerUI;

/**
 *
 * @author ejunk
 */
public class RoundSpinner extends JSpinner {

    private JButton minusButton;
    private JButton plusButton;
    private JTextField amountField;
    private JPanel spinnerPanel;
    private final int borderRadius = 10; // Radius for rounded corners

    public RoundSpinner(int initialValue, int minValue, int maxValue, int step) {
        initComponents(initialValue, minValue, maxValue, step);
        customizeComponents();
    }

    public RoundSpinner() {
        initComponents(1, 1, 10, 1);
        customizeComponents();
    }

    private void initComponents(int initialValue, int minValue, int maxValue, int step) {
        // Create the main panel with BorderLayout
        spinnerPanel = new JPanel(new BorderLayout(5, 0));
        spinnerPanel.setOpaque(false);

        // Create buttons with icons
        minusButton = new JButton(createMinusIcon());
        plusButton = new JButton(createPlusIcon());

        // Create a custom text field for the amount
        amountField = new JTextField(String.valueOf(initialValue), 3);
        amountField.setHorizontalAlignment(JTextField.CENTER);
        amountField.setEditable(false); // Make it read-only for now
        amountField.setBorder(new RoundedBorder(borderRadius)); // Apply rounded border
        amountField.setBackground(new Color(245, 245, 245)); // Light gray background
        amountField.setForeground(Color.DARK_GRAY); // Dark text
        amountField.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Set button properties
        minusButton.setBorder(new RoundedBorder(borderRadius));
        plusButton.setBorder(new RoundedBorder(borderRadius));
        minusButton.setContentAreaFilled(false); // No background fill
        plusButton.setContentAreaFilled(false); // No background fill
        minusButton.setFocusPainted(false);
        plusButton.setFocusPainted(false);
        minusButton.setForeground(new Color(0, 120, 215)); // Blue color for icons
        plusButton.setForeground(new Color(0, 120, 215)); // Blue color for icons
        minusButton.setOpaque(false);
        plusButton.setOpaque(false);

        // Add action listeners
        minusButton.addActionListener(e -> adjustValue(-step));
        plusButton.addActionListener(e -> adjustValue(step));

        // Layout the components
        spinnerPanel.add(minusButton, BorderLayout.WEST);
        spinnerPanel.add(amountField, BorderLayout.CENTER);
        spinnerPanel.add(plusButton, BorderLayout.EAST);

        // Set the custom UI and editor
        setUI(new CustomSpinnerUI());
        setEditor(new JSpinner.DefaultEditor(this) {
            @Override
            public void stateChanged(javax.swing.event.ChangeEvent e) {
                amountField.setText(String.valueOf(getValue()));
            }
        });
        setValue(initialValue);
    }

    private void customizeComponents() {
        // Apply custom border to the spinner panel
        spinnerPanel.setBorder(new RoundedBorder(borderRadius));
        spinnerPanel.setBackground(new Color(255, 255, 255)); // White background
    }

    // Adjust the value based on step and bounds
    private void adjustValue(int delta) {
        int currentValue = (Integer) getValue();
        int newValue = currentValue + delta;
        SpinnerNumberModel model = (SpinnerNumberModel) getModel();
        int min = model.getMinimum() != null ? (Integer) model.getMinimum() : Integer.MIN_VALUE;
        int max = model.getMaximum() != null ? (Integer) model.getMaximum() : Integer.MAX_VALUE;

        if (newValue >= min && newValue <= max) {
            setValue(newValue);
        }
    }

    // Create a minus icon
    private Icon createMinusIcon() {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(minusButton.getForeground());
                int width = getIconWidth();
                int height = getIconHeight();
                g2d.fillRect(x + 4, y + height / 2, width - 8, 2); // Horizontal line for minus
                g2d.dispose();
            }

            @Override
            public int getIconWidth() {
                return 16;
            }

            @Override
            public int getIconHeight() {
                return 16;
            }
        };
    }

    // Create a plus icon
    private Icon createPlusIcon() {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(plusButton.getForeground());
                int width = getIconWidth();
                int height = getIconHeight();
                g2d.fillRect(x + 4, y + height / 2, width - 8, 2); // Horizontal line
                g2d.fillRect(x + width / 2, y + 4, 2, height - 8); // Vertical line
                g2d.dispose();
            }

            @Override
            public int getIconWidth() {
                return 16;
            }

            @Override
            public int getIconHeight() {
                return 16;
            }
        };
    }

    // Custom UI to hide default arrows and use our panel
    private class CustomSpinnerUI extends BasicSpinnerUI {

        @Override
        protected JComponent createEditor() {
            return spinnerPanel;
        }

        @Override
        protected Component createPreviousButton() {
            return new JLabel(); // Hide default button
        }

        @Override
        protected Component createNextButton() {
            return new JLabel(); // Hide default button
        }
    }

    // Custom border for rounded corners
    private class RoundedBorder implements Border {

        private final int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius, radius, radius, radius);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(new Color(200, 200, 200)); // Light gray border
            g2d.draw(new RoundRectangle2D.Double(x, y, width - 1, height - 1, radius, radius));
            g2d.dispose();
        }
    }
}
