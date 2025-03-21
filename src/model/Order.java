package model;

import enums.OrderStatus;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    private int orderId;
    private Customer customer;
    private List<Item> items;
    private double totalPrice;
    private OrderStatus status;
    private Date orderTime;

    public Order(int orderId, Customer customer, List<Item> items,
            double totalPrice, OrderStatus status, Date orderTime) {
        this.orderId = orderId;
        this.customer = customer;
        this.items = items;
        this.totalPrice = totalPrice;
        this.status = status;
        this.orderTime = orderTime;
    }

    public int getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<Item> getItems() {
        return items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    @Override
    public String toString() {
        return "Order{"
                + "orderId=" + orderId
                + ", customer=" + customer.getUsername()
                + ", totalPrice=" + totalPrice
                + ", status=" + status
                + ", orderTime=" + orderTime
                + '}';
    }
}
