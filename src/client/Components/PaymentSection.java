package client.Components;

import client.OrderClient;
import java.awt.BorderLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import model.AuthToken;
import model.Payment;
import model.Products;

public class PaymentSection extends JPanel {

    @SuppressWarnings("unused")
    private final int IMAGE_SIZE = 64;
    private JTable productTable;
    private DefaultTableModel tableModel;
    @SuppressWarnings("unused")
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
        String[] columns = {"ID", "Order ID", "Payment Date", "Paid Amount", "Payment Method", "Transaction ID", "Payment Status", "Payment Information"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        productTable = new JTable(tableModel);
//        productTable.setRowHeight(IMAGE_SIZE + 10);
//        productTable.getColumnModel().getColumn(1).setCellRenderer(new ImageRenderer());

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

        OrderClient orderClient = new OrderClient(token);
        SwingWorker<List<Payment>, Void> newcomersWorker = new SwingWorker<>() {
            @Override
            protected List<Payment> doInBackground() throws Exception {

                List<Payment> payments = orderClient.fetechAllPayment(token);

//                if (payments != null && !payments.isEmpty()) {
//                    for (Payment payment : payments) {
//                        System.out.println(payment);
//                    }
//                } else {
//                    System.out.println("No payments found or failed to fetch data.");
//                }
                return payments;
            }

            protected void done() {
                try {
                    List<Payment> payments = get();
                    for (Payment payment : payments) {
                        addProductToTable(payment, -1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        newcomersWorker.execute();
    }

    private void addProductToTable(Payment payment, int index) {

        Object[] row = {
            payment.getPaymentId(),
            payment.getOrderId(),
            payment.getPaymentDate(),
            payment.getAmountPaid(),
            payment.getPaymentMethod(),
            payment.getTransactionId(),
            payment.getPaymentStatus(),
            payment.getPaymentInfo().toString()
        };

        if (index >= 0) {
            tableModel.insertRow(index, row);
        } else {
            tableModel.addRow(row);
        }
    }

    // Image renderer for table
//    class ImageRenderer extends DefaultTableCellRenderer {
//
//        JLabel label = new JLabel();
//
//        @Override
//        public Component getTableCellRendererComponent(JTable table, Object value,
//                boolean isSelected, boolean hasFocus, int row, int column) {
//            label.setIcon((ImageIcon) value);
//            label.setHorizontalAlignment(JLabel.CENTER);
//            return label;
//        }
//    }
}
