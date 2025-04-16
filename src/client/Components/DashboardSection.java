package client.Components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class DashboardSection extends JPanel {
    public DashboardSection() {
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel metricsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        metricsPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        metricsPanel.add(createMetricCard("42", "Total Scans", "0.5% ↑"));
        metricsPanel.add(createMetricCard("23", "Total Events", "0.5% ↑"));
        metricsPanel.add(createMetricCard("11", "Total News", "0.5% ↑"));
        metricsPanel.add(createMetricCard("11", "Total Users", "0.5% ↑"));

        add(metricsPanel, BorderLayout.CENTER);
    }

    private JPanel createMetricCard(String value, String title, String percentage) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.LIGHT_GRAY),
                new EmptyBorder(20, 20, 20, 20))
        );
        card.setBackground(Color.WHITE);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel percentageLabel = new JLabel(percentage);
        percentageLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        percentageLabel.setForeground(new Color(0, 128, 0));
        percentageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(valueLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(percentageLabel);

        return card;
    }
}
