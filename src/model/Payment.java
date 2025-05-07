
package model;

import enums.PaymentStatus;
import java.io.Serializable;
import java.util.Map;


public class Payment implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private int paymentId;
    private int orderId;
    private String paymentDate; 
    private double amountPaid;
    private String paymentMethod;
    private String transactionId;
    private PaymentStatus paymentStatus;
    private Order order;
    private Map<String, String> paymentInfo;


    public Payment() {}
    
    //for insert
    public Payment(int orderId, double amountPaid, 
               String paymentMethod, String transactionId, PaymentStatus paymentStatus, Map<String, String> paymentInfo) {
    //    this.paymentId = paymentId;
        this.orderId = orderId;
        this.amountPaid = amountPaid;
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
        this.paymentStatus = paymentStatus;
        this.paymentInfo = paymentInfo;
    }

    //for read
    public Payment(int paymentId, int orderId, String paymentDate, double amountPaid, 
                   String paymentMethod, String transactionId, PaymentStatus paymentStatus, Order order, Map<String, String> paymentInfo) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.paymentDate = paymentDate;
        this.amountPaid = amountPaid;
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
        this.paymentStatus = paymentStatus;
        this.order = order;
        this.paymentInfo = paymentInfo;
    }

    public int getPaymentId() {return paymentId;}
    public int getOrderId() {return orderId;}
    public String getPaymentDate() {return paymentDate;}
    public double getAmountPaid() { return amountPaid;}
    public String getPaymentMethod() {return paymentMethod;}
    public String getTransactionId() { return transactionId;}
    public PaymentStatus getPaymentStatus() {return paymentStatus;}

    public void setPaymentId(int paymentId) {this.paymentId = paymentId;}
    public void setOrderId(int orderId) {this.orderId = orderId;}
    public void setPaymentDate(String paymentDate) {this.paymentDate = paymentDate;}
    public void setAmountPaid(double amountPaid) {this.amountPaid = amountPaid;}
    public void setPaymentMethod(String paymentMethod) {this.paymentMethod = paymentMethod;}
    public void setTransactionId(String transactionId) {this.transactionId = transactionId;}
    public void setPaymentStatus(PaymentStatus paymentStatus) {this.paymentStatus = paymentStatus;}

    public void setPaymentInfo(Map<String, String> paymentInfo) {
        this.paymentInfo = paymentInfo;
    }
    
    public Map<String, String> getPaymentInfo() {
        return paymentInfo;
    }
    
    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", orderId=" + orderId +
                ", paymentDate='" + paymentDate + '\'' +
                ", amountPaid=" + amountPaid +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                '}';
    }
}
