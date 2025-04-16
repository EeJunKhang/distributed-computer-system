package client.Components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.Products;

public class ProductsSection extends JPanel {

    private final int IMAGE_SIZE = 64;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private List<Products> products = new ArrayList<>();

    public ProductsSection() {
        initializeUI();
        loadSampleData();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Product Management");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Table setup
        String[] columns = {"ID", "Image", "Name", "Price", "Category", "Stock", "Last Updated"};
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

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");

        // Delete button action
        deleteButton.addActionListener(e -> handleDelete());

        // Edit button action
        editButton.addActionListener(e -> handleEdit());

        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleEdit() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow >= 0) {
            Products product = products.get(selectedRow);
            EditProductDialog dialog = new EditProductDialog(
                    (Frame) SwingUtilities.getWindowAncestor(this),
                    product
            );
            if (dialog.showDialog()) {
                updateProduct(selectedRow, dialog.getUpdatedProduct());
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please select a product to edit",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void updateProduct(int index, Products updatedProduct) {
        products.set(index, updatedProduct);
        tableModel.removeRow(index);
        addProductToTable(updatedProduct, index);
    }

    private void handleDelete() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this product?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                products.remove(selectedRow);
                tableModel.removeRow(selectedRow);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Please select a product to delete",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void loadSampleData() {
        products.add(new Products(1, "Laptop", "High performance gaming laptop", 1299.99,
                "Electronics","src/resources/burger.jpg",50, "2024-03-15"));
        products.add(new Products(2, "Smartphone", "Latest model smartphone", 799.99,
                "Electronics", "src/resources/burger.jpg",100, "2024-03-14"));

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
