/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package database.test;

import database.PaymentDAO;
import enums.PaymentStatus;
import java.util.List;
import model.Payment;

/**
 *
 * @author zheng
 */
public class PaymentDAOTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         PaymentDAO paymentDAO = new PaymentDAO();

        // 1. Create a new Payment
        Payment newPayment = new Payment(0, 1, 150.50, "Credit Card", "TXN00123", PaymentStatus.PENDING);
        boolean created = paymentDAO.create(newPayment);
        System.out.println("Created: " + created + ", ID: " + newPayment.getPaymentId());

        // 2. Read the payment
        Payment readPayment = paymentDAO.read(newPayment.getPaymentId());
        System.out.println("Read Payment: " + (readPayment != null ? readPayment : "Not found"));

        // 3. Update the payment
        if (readPayment != null) {
            readPayment.setAmountPaid(199.99);
            readPayment.setPaymentStatus(PaymentStatus.COMPLETED);
            boolean updated = paymentDAO.update(readPayment);
            System.out.println("Updated: " + updated);

            // Re-read after update
            Payment updatedPayment = paymentDAO.read(readPayment.getPaymentId());
            System.out.println("After Update: " + updatedPayment);
        }

        // 4. Get all payments
        List<Payment> allPayments = paymentDAO.getAll();
        System.out.println("All Payments:");
        for (Payment p : allPayments) {
            System.out.println(p);
        }

        // 5. Delete the payment
        boolean deleted = paymentDAO.delete(newPayment.getPaymentId());
        System.out.println("Deleted: " + deleted);

        // 6. Verify deletion
        Payment deletedCheck = paymentDAO.read(newPayment.getPaymentId());
        System.out.println("Deleted Check: " + (deletedCheck == null ? "Successfully deleted" : "Still exists"));
    }
    
}
