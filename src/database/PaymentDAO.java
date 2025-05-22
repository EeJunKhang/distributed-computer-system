package database;

import enums.PaymentStatus;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Order;
import model.Payment;

public class PaymentDAO extends DBOperation<Payment, Integer> {

    public PaymentDAO() {
        super();
    }

    @Override
    public Payment mapResultSetToEntity(ResultSet rs) throws SQLException {

        int id = rs.getInt("payment_id");
        int orderId = rs.getInt("order_id");
        String paymentDate = rs.getString("payment_date");
        double amount = rs.getDouble("amount_paid");
        String method = rs.getString("payment_method");
        String transactionId = rs.getString("transaction_id");
        PaymentStatus status = PaymentStatus.valueOf(rs.getString("payment_status"));
        Order order = new OrderDAO().getOrderById(orderId);
        String paymentInfo = rs.getString("payment_info");

        Map<String, String> parsedMap = new HashMap<>();
        paymentInfo = paymentInfo.substring(1, paymentInfo.length() - 1); // remove braces
        
        for (String entry : paymentInfo.split(", ")) {
            String[] keyValue = entry.split("=", 2);
            parsedMap.put(keyValue[0], keyValue[1]);
        }

        return new Payment(id, orderId, paymentDate, amount, method, transactionId, status, order, parsedMap);
    }

    /**
     *
     * @param payment
     * @return
     */
    @Override
    public boolean create(Payment payment) {
        String sql = "INSERT INTO payments (order_id, amount_paid, payment_method, transaction_id, payment_status, payment_info) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        return executeTransaction(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, payment.getOrderId());
                stmt.setDouble(2, payment.getAmountPaid());
                stmt.setString(3, payment.getPaymentMethod());
                stmt.setString(4, payment.getTransactionId());
                stmt.setString(5, payment.getPaymentStatus().name());
                stmt.setString(6, payment.getPaymentInfo().toString());

                int affectedRows = stmt.executeUpdate();

                if (affectedRows == 0) {
                    return false;
                }

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        payment.setPaymentId(generatedKeys.getInt(1));
                        return true;
                    }
                }

                return false;
            }
        });
    }

    public Integer createPayment(Payment payment) {
        String sql = "INSERT INTO payments (order_id, amount_paid, payment_method, transaction_id, payment_status, payment_info) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        return executeTransaction(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, payment.getOrderId());
                stmt.setDouble(2, payment.getAmountPaid());
                stmt.setString(3, payment.getPaymentMethod());
                stmt.setString(4, payment.getTransactionId());
                stmt.setString(5, payment.getPaymentStatus().name());
                stmt.setString(6, payment.getPaymentInfo().toString());

                int affectedRows = stmt.executeUpdate();

                if (affectedRows == 0) {
                    return null;
                }

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int paymentId = generatedKeys.getInt(1);
                        payment.setPaymentId(paymentId);
                        return paymentId;  // Return the generated payment ID
                    }
                }

                return null;
            }
        });
    }

    @Override
    public Payment read(Integer id) {
        String sql = "SELECT * FROM payments WHERE payment_id = ?";

        return executeTransaction(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToEntity(rs);
                    }
                    return null;
                }
            }
        });
    }

    @Override
    public boolean update(Payment payment) {
        String sql = "UPDATE payments SET order_id = ?, amount_paid = ?, payment_method = ?, "
                + "transaction_id = ?, payment_status = ? WHERE payment_id = ?";

        return executeTransaction(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, payment.getOrderId());
                stmt.setDouble(2, payment.getAmountPaid());
                stmt.setString(3, payment.getPaymentMethod());
                stmt.setString(4, payment.getTransactionId());
                stmt.setString(5, payment.getPaymentStatus().name());
                stmt.setInt(6, payment.getPaymentId());

                return stmt.executeUpdate() > 0;
            }
        });
    }

    public boolean updatePaymentStatus(int paymentId, PaymentStatus paymentStatus) {
        String sql = "UPDATE payments SET payment_status = ? WHERE payment_id = ?";

        return executeTransaction(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, paymentStatus.name());
                stmt.setInt(2, paymentId);

                return stmt.executeUpdate() > 0;
            }
        });
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM payments WHERE payment_id = ?";

        return executeTransaction(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                return stmt.executeUpdate() > 0;
            }
        });
    }

    @Override
    public List<Payment> getAll() {
        String sql = "SELECT * FROM payments";

        return executeTransaction(conn -> {
            List<Payment> payments = new ArrayList<>();
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    payments.add(mapResultSetToEntity(rs));
                }
                return payments;
            }
        });
    }

}
