package model;

public class OrderItem {
    private int orderItemId;
    private Order order;
    private Items item;
    private int quantity;
    private double pricePerUnit;

    public OrderItem() {
    }

    public OrderItem(int orderItemId, Order order, Items item, int quantity, double pricePerUnit) {
        this.orderItemId = orderItemId;
        this.order = order;
        this.item = item;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
    }

    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrderId(Order order) {
        this.order = order;
    }

    public Items getItem() {
        return item;
    }

    public void setItem(Items item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }
}
