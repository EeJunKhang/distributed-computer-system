package model;

import java.io.Serializable;

public class OrderItem implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private int orderItemId;
    private Products item;
    private int quantity;

    @Override
    public String toString(){
        return item.getItemName() + " x" + this.quantity;
    }

    public OrderItem(Products item, int quantity){
        this.item = item;
        this.quantity = quantity;
    }

    public OrderItem(int orderItemId, Products item, int quantity) {
        this.orderItemId = orderItemId;
        this.item = item;
        this.quantity = quantity;
    }

    public OrderItem() {
    }

    public int getOrderItemId() { return orderItemId; }
    public Products getProduct() { return item; }
    public int getQuantity() { return quantity; }

    public void setOrderItemId(int orderItemId) { this.orderItemId = orderItemId; }
    public void setProduct(Products item) { this.item = item; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
   
