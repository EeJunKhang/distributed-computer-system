package model;

import enums.OrderStatus;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    private int orderId;
    private Customer user;
    private String orderTime;
    private OrderStatus status;
    private double totalPrice;
    private List<OrderItem> items;
    
    public Order(){
        
    }
    
    public Order(Customer user, OrderStatus status, double totalPrice, List<OrderItem> items){
        this.user = user;
        this.items = items;
        this.totalPrice = totalPrice;
        this.status = status;
    }
    
    public Order(int orderId, Customer user, String orderTime, 
            OrderStatus status,
            double totalPrice, List<OrderItem> items) {
        this.orderId = orderId;
        this.user = user;
        this.items = items;
        this.totalPrice = totalPrice;
        this.status = status;
        this.orderTime = orderTime;
    }

    public int getOrderId() {
        return orderId;
    }

    public Customer getUser() {
        return user;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setUser(Customer user) {
        this.user = user;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    @Override
    public String toString() {
        return "Order{"
                + "orderId=" + orderId
                + ", customer=" + user.getUsername()
                + ", totalPrice=" + totalPrice
                + ", status=" + status
                + ", orderTime=" + orderTime
                + '}';
    }
}
