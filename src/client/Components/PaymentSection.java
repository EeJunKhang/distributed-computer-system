package client.Components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.AuthToken;
import model.Products;

public class PaymentSection extends JPanel {

    private final int IMAGE_SIZE = 64;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private List<Products> products = new ArrayList<>();
    private AuthToken token;

    public PaymentSection(AuthToken token) {
        this.token = token;
        initializeUI();
        loadSampleData();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Payment Order");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Table setup
        String[] columns = {"ID", "Order ID", "Payment Date", "Paid Amount", "Payment Method", "Transaction ID", "Payment Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 1 ? ImageIcon.class : Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        productTable = new JTable(tableModel);
        productTable.setRowHeight(IMAGE_SIZE + 10);
        productTable.getColumnModel().getColumn(1).setCellRenderer(new ImageRenderer());

        JScrollPane scrollPane = new JScrollPane(productTable);
        add(scrollPane, BorderLayout.CENTER);

//        // Buttons panel
//        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//        JButton editButton = new JButton("Edit");
//        JButton deleteButton = new JButton("Delete");
//
//        // Delete button action
//        deleteButton.addActionListener(e -> handleDelete());
//
//        // Edit button action
//        editButton.addActionListener(e -> handleEdit());
//        buttonPanel.add(editButton);
//        buttonPanel.add(deleteButton);
//        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadSampleData() {
        products.add(new Products(1, "Laptop", "High performance gaming laptop", 1299.99,
                "Electronics", "src/resources/burger.jpg", 50, "2024-03-15"));
        products.add(new Products(2, "Smartphone", "Latest model smartphone", 799.99,
                "Electronics", "src/resources/burger.jpg", 100, "2024-03-14"));

        for (Products product : products) {
            addProductToTable(product, -1);
        }
    }

    private void addProductToTable(Products product, int index) {
        ImageIcon icon = null;
        if (product.getImage() != null && !product.getImage().isEmpty()) {
            icon = new ImageIcon(new ImageIcon(product.getImage())
                    .getImage().getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_SMOOTH));
        }

        Object[] row = {
            product.getId(),
            icon,
            product.getItemName(),
            product.getPrice(),
            product.getCategory(),
            product.getStockQuantity(),
            product.getLastUpdated()
        };

        if (index >= 0) {
            tableModel.insertRow(index, row);
        } else {
            tableModel.addRow(row);
        }
    }

    // Image renderer for table
    class ImageRenderer extends DefaultTableCellRenderer {

        JLabel label = new JLabel();

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            label.setIcon((ImageIcon) value);
            label.setHorizontalAlignment(JLabel.CENTER);
            return label;
        }
    }
}
