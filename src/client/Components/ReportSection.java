package client.Components;

import client.OrderClient;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.print.PrinterException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.border.EmptyBorder;
import model.AuthToken;
import model.Order;
import model.OrderItem;

public class ReportSection extends JPanel {

    private List<Order> orders;
    private DefaultTableModel tableModel;
    private AuthToken token;
    private JPanel loadingPanel;
    private JPanel contentPanel;
    private JButton reportButton;
    private JButton printButton;
    private JButton updateButton;

    public ReportSection(AuthToken token) {
        this.token = token;
        initializeUI();
        fetchData();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Create loading panel
        loadingPanel = new JPanel(new BorderLayout());
        JLabel loadingLabel = new JLabel("Loading orders...", JLabel.CENTER);
        loadingLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        loadingPanel.add(loadingLabel, BorderLayout.CENTER);
        loadingPanel.add(new JProgressBar(), BorderLayout.SOUTH);

        // Create content panel (initially empty)
        contentPanel = new JPanel(new BorderLayout());

        // Add both panels (loading will be shown first)
        add(loadingPanel, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER); // Will be made visible later

        // Initially show loading panel
        loadingPanel.setVisible(true);
        contentPanel.setVisible(false);
    }

    private void fetchData() {
        OrderClient a = new OrderClient(token);
        SwingWorker<List<Order>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Order> doInBackground() throws Exception {
                // Simulate network delay
                return a.fetchAllOrders(token);
            }

            @Override
            protected void done() {
                try {
                    orders = get(); // Get the fetched orders
                    if (orders == null) {
                        showError("Failed to load orders");
                        return;
                    }
                    setupContentUI(); // Setup the actual UI with data
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    showError("Operation was interrupted");
                } catch (ExecutionException e) {
                    showError("Failed to load orders: " + e.getCause().getMessage());
                }
            }
        };

        worker.execute();
    }

    private void setupContentUI() {
        // Hide loading panel
        loadingPanel.setVisible(false);

        // Clear existing content
        contentPanel.removeAll();

        // Title
        JLabel titleLabel = new JLabel("Order Management");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        contentPanel.add(titleLabel, BorderLayout.NORTH);

        // Create table model
        String[] columns = {"Order ID", "User", "Order Time", "Status", "Total Price"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        populateTable();

        JTable table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true);
        JScrollPane scrollPane = new JScrollPane(table);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        reportButton = new JButton("View Sales Report");
        reportButton.addActionListener(this::generateSalesReport);
        printButton = new JButton("Print Sales Report");
        printButton.addActionListener(this::printSalesReport);
        updateButton = new JButton("Update Order Status");
        updateButton.addActionListener(this::updateOrderStatus);
        buttonPanel.add(updateButton);
        buttonPanel.add(reportButton);
        buttonPanel.add(printButton);
        
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Show content panel
        contentPanel.setVisible(true);
        revalidate();
        repaint();
    }
    
    private void updateOrderStatus(ActionEvent e){
        
    }
    
    private void showError(String message) {
        loadingPanel.setVisible(false);

        JPanel errorPanel = new JPanel(new BorderLayout());
        JLabel errorLabel = new JLabel(message, JLabel.CENTER);
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        JButton retryButton = new JButton("Retry");
        retryButton.addActionListener(e -> {
            remove(errorPanel);
            add(loadingPanel, BorderLayout.CENTER);
            loadingPanel.setVisible(true);
            fetchData();
        });

        errorPanel.add(errorLabel, BorderLayout.CENTER);
        errorPanel.add(retryButton, BorderLayout.SOUTH);

        add(errorPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void populateTable() {
        if (orders == null) {
            return;
        }

        tableModel.setRowCount(0); // Clear existing data

        for (Order order : orders) {
            Object[] rowData = {
                order.getOrderId(),
                order.getUser().getFullName(),
                order.getOrderTime(),
                order.getStatus().toString(),
                String.format("RM %.2f", order.getTotalPrice())
            };
            tableModel.addRow(rowData);
        }
    }

    private void generateSalesReport(ActionEvent e) {
        if (orders == null || orders.size() < 1) {
            JOptionPane.showMessageDialog(
                    this,
                    "No Order is found",
                    "Sales Report",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        JEditorPane editorPane = generateReportHTML();
        JScrollPane scrollPane = new JScrollPane(editorPane);
        scrollPane.setPreferredSize(new Dimension(800, 600));

        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "Sales Report",
                JOptionPane.PLAIN_MESSAGE
        );
    }

    private JEditorPane generateReportHTML() {
        double totalSales = 0;
        int totalQuantity = 0;

        // Calculate totals
        for (Order order : orders) {
            totalSales += order.getTotalPrice();
            for (OrderItem item : order.getItems()) {
                totalQuantity += item.getQuantity();
            }
        }

        // Create HTML content
        StringBuilder html = new StringBuilder("<html><body style='padding:20px'>");
        html.append("<h1 style='color:#2c3e50'>Sales Report</h1>");
        html.append("<table border='1' cellpadding='8' style='border-collapse:collapse;width:100%'>");
        html.append("<tr bgcolor='#f2f2f2'><th>Order ID</th><th>User</th><th>Order Time</th><th>Status</th><th>Total</th></tr>");

        for (Order order : orders) {
            html.append("<tr>")
                    .append("<td>").append(order.getOrderId()).append("</td>")
                    .append("<td>").append(order.getUser().getFullName()).append("</td>")
                    .append("<td>").append(order.getOrderTime()).append("</td>")
                    .append("<td>").append(order.getStatus()).append("</td>")
                    .append("<td style='text-align:right'>").append(String.format("RM %.2f", order.getTotalPrice())).append("</td>")
                    .append("</tr>");
        }

        html.append("</table>");

        // Add summary section
        html.append("<div style='margin-top:20px; background-color:#f8f9fa; padding:15px; border-radius:5px'>")
                .append("<h3 style='color:#27ae60'>Summary</h3>")
                .append("<p>Total Sales: <b>").append(String.format("RM %.2f", totalSales)).append("</b></p>")
                .append("<p>Total Order: <b>").append(orders.size()).append("</b></p>")
                .append("<p>Total Items Sold: <b>").append(totalQuantity).append("</b></p>")
                .append("</div>")
                .append("</body></html>");

        // Display report in dialog
        JEditorPane editorPane = new JEditorPane("text/html", html.toString());
        editorPane.setEditable(false);
        return editorPane;
    }

    private void printSalesReport(ActionEvent e) {
        if (orders == null || orders.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No orders found to print",
                    "Print Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        JEditorPane editorPane = generateReportHTML();
        editorPane.setSize(800, 600);
        editorPane.setPreferredSize(new Dimension(800, 600));

        // Make the editor pane printable
        try {
            boolean complete = editorPane.print();
            if (complete) {
                JOptionPane.showMessageDialog(
                        this,
                        "Printing completed successfully",
                        "Print Status",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Printing was cancelled",
                        "Print Status",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Failed to print report: " + ex.getMessage(),
                    "Print Error",
                    JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }
}
