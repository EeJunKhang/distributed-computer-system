
package database;

import enums.PaymentStatus;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Order;
import model.Payment;


public class PaymentDAO extends DBOperation<Payment,Integer>{
    
    public PaymentDAO(){
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
        return new Payment(id,orderId, paymentDate,amount, method, transactionId, status, order);
    }
    
    @Override
    public boolean create(Payment payment) {
        String sql = "INSERT INTO payment (order_id, amount_paid, payment_method, transaction_id, payment_status) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        return executeTransaction(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, payment.getOrderId());
                stmt.setDouble(2, payment.getAmountPaid());
                stmt.setString(3, payment.getPaymentMethod());
                stmt.setString(4, payment.getTransactionId());
                stmt.setString(5, payment.getPaymentStatus().name());

                int affectedRows = stmt.executeUpdate();

                if (affectedRows == 0) return false;

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

    @Override
    public Payment read(Integer id) {
        String sql = "SELECT * FROM payment WHERE payment_id = ?";

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
        String sql = "UPDATE payment SET order_id = ?, amount_paid = ?, payment_method = ?, " +
                     "transaction_id = ?, payment_status = ? WHERE payment_id = ?";

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

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM payment WHERE payment_id = ?";

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
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    payments.add(mapResultSetToEntity(rs));
                }
                return payments;
            }
        });
    }

    
    
}
