/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client.Components;

import enums.OrderStatus;
import java.awt.BorderLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import model.Customer;
import model.Order;
import model.OrderItem;
import model.Products;

/**
 *
 * @author ejunk
 */
public class OrderHistorySection extends JPanel {

    private JTable orderTable;
    private DefaultTableModel tableModel;
    private List<Order> orders = new ArrayList<>();

    public OrderHistorySection() {
        initializeUI();
        loadSampleData();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel("Order History");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Table setup
        String[] columns = {"Order ID", "Customer", "Order Time", "Status", "Total Price", "Items"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        orderTable = new JTable(tableModel);
        orderTable.setRowHeight(30);
        orderTable.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(orderTable);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadSampleData() {
        // Create sample products
        Products laptop = new Products(1, "Laptop", "s", 999.99, "accessory", "src/resources/burger.jpg", 3, "23/4/532");
        Products mouse = new Products(2, "mouse", "this is a mouse", 99.99, "accessories", "src/resources/burger.jpg", 30, "20/5/532");

        // get current User
        Customer u = new Customer(1, "s", "s", "s", "s", "s", "s", "s", "s");
        // Create sample orders
        var items = List.of(
                new OrderItem(laptop, 10),
                new OrderItem(mouse, 5)
        );

        Order order1 = new Order(1001, u, "order time here", OrderStatus.CANCELED, 23.23, items);

        var items2 = List.of(
                new OrderItem(laptop, 3),
                new OrderItem(mouse, 2)
        );

        Order order2 = new Order(1002, u, "order time here", OrderStatus.DELIVERED, 23.23, items2);

        orders.add(order1);
        orders.add(order2);

        // Populate table
        for (Order order : orders) {
            Object[] row = {
                order.getOrderId(),
                order.getUser().getFullName(),
                order.getOrderTime(),
                order.getStatus().toString(),
                String.format("$%.2f", order.getTotalPrice()),
                formatOrderItems(order.getItems())
            };
            tableModel.addRow(row);
        }
    }

    private String formatOrderItems(List<OrderItem> items) {
        StringBuilder sb = new StringBuilder();
        for (OrderItem item : items) {
            sb.append(item.getProduct().getItemName())
                    .append(" x")
                    .append(item.getQuantity())
                    .append(", ");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2); // Remove last comma and space
        }
        return sb.toString();
    }
}
