package client.Components;

import client.OrderClient;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import model.AuthToken;
import model.Order;
import model.OrderItem;
import model.User;

/**
 *
 * @author ejunk
 */
public class OrderHistorySection extends JPanel {

    private JTable orderTable;
    private DefaultTableModel tableModel;
    @SuppressWarnings("unused")
    private List<Order> orders = new ArrayList<>();
    private User user;
    private AuthToken token;
    private JScrollPane scrollPane;
    private JLabel titleLabel;

    public OrderHistorySection(User user, AuthToken token) {
        this.user = user;
        this.token = token;
        initializeUI();
        loadOrderHistory();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        titleLabel = new JLabel("Order History");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"Order ID", "Order Time", "Status", "Total Price", "Items"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        orderTable = new JTable(tableModel);
        orderTable.setRowHeight(30);
        orderTable.setAutoCreateRowSorter(true);
        scrollPane = new JScrollPane(orderTable);
    }

    public void refreshHistoryData() {
        showLoadingPanel();
        loadOrderHistory();
    }

    private void showLoadingPanel() {
        removeAll();
        add(titleLabel, BorderLayout.NORTH);
        add(createLoadingPanel(), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void loadOrderHistory() {
        if (token == null) {
            return;
        }

        OrderClient orderClient = new OrderClient(token);
        SwingWorker<List<Order>, Void> orderById = new SwingWorker<>() {
            @Override
            protected List<Order> doInBackground() throws Exception {
                return orderClient.fetchOrderByUserId(token, user.getUserId());
            }

            @Override
            protected void done() {
                try {
                    List<Order> customerOrders = get(); 
                    displayOrders(customerOrders);
                } catch (Exception e) {
                    e.printStackTrace(); 
                }
            }
        };

        orderById.execute();
    }


    private void displayOrders(List<Order> customerOrders) {
        tableModel.setRowCount(0);

        for (Order order : customerOrders) {
            Object[] row = {
                order.getOrderId(),
                order.getOrderTime(),
                order.getStatus().toString(),
                String.format("RM %.2f", order.getTotalPrice()),
                formatOrderItems(order.getItems())
            };
            tableModel.addRow(row);
        }

        removeAll();
        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        revalidate();
        repaint();
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
            sb.setLength(sb.length() - 2);
        }
        return sb.toString();
    }

    private JPanel createLoadingPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        JLabel label = new JLabel("Loading...", SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }
}
