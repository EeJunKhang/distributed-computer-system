package client.Components;

import client.ProductClient;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.AuthToken;
import model.Products;
import utils.ImagePathGetter;

public class ProductsSection extends JPanel {

    private final int IMAGE_SIZE = 64;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private List<Products> products = new ArrayList<>();
    private AuthToken token;

    public ProductsSection(AuthToken token) {
        this.token = token;
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        showLoadingScreen(); // Show loading first
        loadData();
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
    }

    private void showLoadingScreen() {
        removeAll(); // Clear any existing components
        setLayout(new BorderLayout());

        JLabel loadingLabel = new JLabel("Loading products...", JLabel.CENTER);
        loadingLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(loadingLabel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private void showErrorScreen(String message) {
        removeAll();
        setLayout(new BorderLayout());

        JLabel errorLabel = new JLabel(message, JLabel.CENTER);
        errorLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        errorLabel.setForeground(Color.RED);

        JButton retryButton = new JButton("Retry");
        retryButton.addActionListener(_ -> {
            showLoadingScreen();
            loadData();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(retryButton);

        add(errorLabel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private void showMainUI() {
        removeAll();
        initializeUI(); // This will create the complete UI with title, table, and buttons
    }

    private void initializeUI() {

        // Title
        JLabel titleLabel = new JLabel("Product Management");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        productTable = new JTable(tableModel);
        productTable.setRowHeight(IMAGE_SIZE + 10);
        productTable.getColumnModel().getColumn(1).setCellRenderer(new ImageRenderer());

        JScrollPane scrollPane = new JScrollPane(productTable);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        JButton addButton = new JButton("Add");

        // Delete button action
        deleteButton.addActionListener(_ -> handleDelete());

        // Edit button action
        editButton.addActionListener(_ -> handleEdit());

        addButton.addActionListener(_ -> handleAdd());

        buttonPanel.add(addButton);
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
                    product,
                    token
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
               
                
                Object idValue = productTable.getValueAt(selectedRow, 0); 
                int productId = Integer.parseInt(idValue.toString());
                

            
                ProductClient client = new ProductClient(token);
                client.deleteProduct(productId);
                
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

    private void handleAdd() {
        AddProductDialog dialog = new AddProductDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                token
        );
        dialog.setVisible(true);
    }

    private void loadData() {
        // Create a loading panel that covers the whole section
        SwingWorker<List<Products>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Products> doInBackground() throws Exception {
                ProductClient client = new ProductClient(token);
                return client.fetchAllProduct();
            }

            @Override
            protected void done() {
                try {
                    List<Products> result = get();

                    if (result != null && !result.isEmpty()) {
                        products = result;
                        for (Products product : products) {
                            addProductToTable(product, -1);
                        }
                        showMainUI(); // Show the complete UI only after data is loaded
                    } else {
                        showErrorScreen("No products found or error occurred while fetching data.");
                    }
                } catch (InterruptedException | ExecutionException e) {
                    showErrorScreen("Error fetching products: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

//    private void showErrorMessage(String message) {
//        // Clear the current view
//        this.removeAll();
//
//        // Create error panel
//        JPanel errorPanel = new JPanel(new BorderLayout());
//        JLabel errorLabel = new JLabel(message, JLabel.CENTER);
//        errorLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
//        errorLabel.setForeground(Color.RED);
//
//        // Add retry button
//        JButton retryButton = new JButton("Retry");
//        retryButton.addActionListener(e -> {
//            this.removeAll();
//            initializeUI(); // Re-initialize the UI
//            loadData();     // Try loading again
//        });
//
//        JPanel buttonPanel = new JPanel();
//        buttonPanel.add(retryButton);
//
//        errorPanel.add(errorLabel, BorderLayout.CENTER);
//        errorPanel.add(buttonPanel, BorderLayout.SOUTH);
//
//        this.add(errorPanel, BorderLayout.CENTER);
//        this.revalidate();
//        this.repaint();
//    }
    private void addProductToTable(Products product, int index) {
        ImageIcon icon = null;
        if (product.getImage() != null && !product.getImage().isEmpty()) {
            icon = new ImageIcon(new ImageIcon(ImagePathGetter.getImageFullPath(product.getImage()))
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
