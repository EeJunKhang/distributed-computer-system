/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client.Components;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import model.Order;
import model.OrderItem;

/**
 *
 * @author ejunk
 */
public class PaymentDialog extends JDialog {
    private final Order order;
    private JTextField cardNumberField;
    private JTextField expiryField;
    private JTextField cvvField;
    private JComboBox<String> bankComboBox;
    private JTextField accountNumberField;
    private JTextField accountNameField;

    public PaymentDialog(Frame parent, Order order) {
        super(parent, "Payment Processing", true);
        this.order = order;
        initializeUI();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initializeUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Order Summary
        mainPanel.add(createOrderSummaryPanel(), BorderLayout.NORTH);

        // Payment Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Credit Card", createCreditCardPanel());
        tabbedPane.addTab("Bank Transfer", createBankTransferPanel());
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Confirm Button
        JButton confirmButton = new JButton("Confirm Payment");
        confirmButton.addActionListener(this::processPayment);
        mainPanel.add(confirmButton, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createOrderSummaryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Order Summary"));

        // Table for order items
        String[] columns = {"Item", "Quantity", "Price", "Subtotal"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (OrderItem item : order.getItems()) {
            model.addRow(new Object[]{
                item.getProduct().getItemName(),
                item.getQuantity(),
                String.format("RM%.2f", item.getProduct().getPrice()),
                String.format("RM%.2f", item.getQuantity() * item.getProduct().getPrice())
            });
        }

        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        // Total amount
        JPanel totalPanel = new JPanel();
        totalPanel.setLayout(new BoxLayout(totalPanel, BoxLayout.PAGE_AXIS));
        JLabel subTotalLabel = new JLabel("Sub Total Amount: " + 
            String.format("RM%.2f", order.getTotalPrice()));
        subTotalLabel.setFont(subTotalLabel.getFont().deriveFont(Font.BOLD, 14f));
        totalPanel.add(subTotalLabel);
        
        JLabel deliveryLabel = new JLabel("Delivery Amount: RM 5");
        deliveryLabel.setFont(deliveryLabel.getFont().deriveFont(Font.BOLD, 14f));
        totalPanel.add(deliveryLabel);
        
        JLabel totalLabel = new JLabel("Total Amount: " + 
            String.format("RM%.2f", order.getTotalPrice() + 5));
        totalLabel.setFont(totalLabel.getFont().deriveFont(Font.BOLD, 14f));
        totalPanel.add(totalLabel);
        
        panel.add(totalPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createCreditCardPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Card Number
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Card Number:"), gbc);
        gbc.gridx = 1;
        cardNumberField = new JTextField(20);
        panel.add(cardNumberField, gbc);

        // Expiry Date
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Expiry (MM/YY):"), gbc);
        gbc.gridx = 1;
        expiryField = new JTextField(5);
        panel.add(expiryField, gbc);

        // CVV
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("CVV:"), gbc);
        gbc.gridx = 1;
        cvvField = new JTextField(3);
        panel.add(cvvField, gbc);

        return panel;
    }

    private JPanel createBankTransferPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Bank Selection
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Select Bank:"), gbc);
        gbc.gridx = 1;
        bankComboBox = new JComboBox<>(new String[]{
            "CIMB", "MayBank", "OCBC Bank", "Public Bank" ,"Other"
        });
        panel.add(bankComboBox, gbc);

        // Account Number
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Account Number:"), gbc);
        gbc.gridx = 1;
        accountNumberField = new JTextField(20);
        panel.add(accountNumberField, gbc);

        // Account Name
        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel("Account Name:"), gbc);
        gbc.gridx = 1;
        accountNameField = new JTextField(20);
        panel.add(accountNameField, gbc);

        return panel;
    }

    private void processPayment(ActionEvent e) {
        // Get selected payment details
        if (bankComboBox.getParent().isVisible()) { // If bank transfer tab is visible
            String bank = (String) bankComboBox.getSelectedItem();
            String accountNumber = accountNumberField.getText();
            String accountName = accountNameField.getText();
            
            // Validate and process bank transfer
            if (validateBankTransfer(bank, accountNumber, accountName)) {
                JOptionPane.showMessageDialog(this, "Bank transfer processed successfully!");
                dispose();
            }
        } else { // Credit card tab
            String cardNumber = cardNumberField.getText();
            String expiry = expiryField.getText();
            String cvv = cvvField.getText();
            
            if (validateCreditCard(cardNumber, expiry, cvv)) {
                JOptionPane.showMessageDialog(this, "Credit card payment processed successfully!");
                dispose();
            }
        }
    }

    private boolean validateCreditCard(String cardNumber, String expiry, String cvv) {
        // Add proper validation logic here
        return !cardNumber.isEmpty() && !expiry.isEmpty() && !cvv.isEmpty();
    }

    private boolean validateBankTransfer(String bank, String accountNumber, String accountName) {
        // Add proper validation logic here
        return !bank.isEmpty() && !accountNumber.isEmpty() && !accountName.isEmpty();
    } 
}
