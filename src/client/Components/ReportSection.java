package client.Components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.border.EmptyBorder;
import model.Order;
import model.OrderItem;

public class ReportSection extends JPanel {

    private List<Order> orders;
    private DefaultTableModel tableModel;

    public ReportSection() {
        initializeUI();
    }

    public ReportSection(List<Order> orders) {
        this.orders = orders;
        initializeUI();
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Order Management");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);
        
        // Create table model
        String[] columns = {"Order ID", "User", "Order Time", "Status", "Total Price"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        populateTable();

        JTable table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JButton reportButton = new JButton("Generate Sales Report");
        reportButton.addActionListener(this::generateSalesReport);
        add(reportButton, BorderLayout.SOUTH);
    }

    private void populateTable() {
        if (orders == null) {
            return;
        }
        for (Order order : orders) {
            Object[] rowData = {
                order.getOrderId(),
                order.getUser().getFullName(), // Assume User has getName()
                order.getOrderTime(),
                order.getStatus().toString(),
                String.format("$%.2f", order.getTotalPrice())
            };
            tableModel.addRow(rowData);
        }
    }

    private void generateSalesReport(ActionEvent e) {
        double totalSales = 0;
        int totalQuantity = 0;

        // Calculate totals
        if (orders != null) {
            for (Order order : orders) {
                totalSales += order.getTotalPrice();
                for (OrderItem item : order.getItems()) {
                    totalQuantity += item.getQuantity();
                }
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
                    .append("<td style='text-align:right'>").append(String.format("$%.2f", order.getTotalPrice())).append("</td>")
                    .append("</tr>");
        }

        html.append("</table>");

        // Add summary section
        html.append("<div style='margin-top:20px; background-color:#f8f9fa; padding:15px; border-radius:5px'>")
                .append("<h3 style='color:#27ae60'>Summary</h3>")
                .append("<p>Total Sales: <b>").append(String.format("$%.2f", totalSales)).append("</b></p>")
                .append("<p>Total Items Sold: <b>").append(totalQuantity).append("</b></p>")
                .append("</div>")
                .append("</body></html>");

        // Display report in dialog
        JEditorPane editorPane = new JEditorPane("text/html", html.toString());
        editorPane.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(editorPane);
        scrollPane.setPreferredSize(new Dimension(800, 600));

        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "Sales Report",
                JOptionPane.PLAIN_MESSAGE
        );
    }
}
