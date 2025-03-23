/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author C
 */
public class Items{
    private int id;
    private String itemName;
    private String itemDescription;
    private double price;
    private String image;
    private String category;
    private int stockQuantity;
    private String lastUpdated;

    public Items(int id, String itemName, String itemDescription, double price, 
                   String category, String image, int stockQuantity, String lastUpdated) {
        this.id = id;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.price = price;
        this.category = category;
        this.image = image;
        this.stockQuantity = stockQuantity;
        this.lastUpdated = lastUpdated;
    }

    public Items() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImageUrl(String image) {
        this.image = image;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    
    @Override
    public String toString() {
    return "Product{" +
            "productId=" + id +
            ", name='" + itemName + '\'' +
            ", description='" + itemDescription + '\'' +
            ", price=" + price +
            ", category='" + category + '\'' +
            ", imageUrl='" + image + '\'' +
            ", stockQuantity=" + stockQuantity +
            ", lastUpdated=" + lastUpdated +
            '}';
}
    
    
    
}
