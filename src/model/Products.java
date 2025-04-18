package model;

import java.io.Serializable;

public class Products implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String itemName;
    private String itemDescription;
    private double price;
    private String image;
    private String category;
    private int stockQuantity;
    private String lastUpdated;
    
    // for fetching data
    public Products(int id, String itemName, String itemDescription, double price, 
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
    // for add new products
    public Products(String itemName,String itemDescription, double price, 
                   String category, String image, int stockQuantity){
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.price = price;
        this.category = category;
        this.image = image;
        this.stockQuantity = stockQuantity;
    }
    public Products() {
    }

    public int getId() { return id; }
    public String getItemName() { return itemName; }
    public String getItemDescription() { return itemDescription; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
    public String getImage() { return image; }
    public int getStockQuantity() { return stockQuantity; }
    public String getLastUpdated() { return lastUpdated; }

    public void setId(int id) { this.id = id; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public void setItemDescription(String itemDescription) { this.itemDescription = itemDescription; }
    public void setPrice(double price) { this.price = price; }
    public void setCategory(String category) { this.category = category; }
    public void setImageUrl(String image) { this.image = image; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
    public void setLastUpdated(String lastUpdated) { this.lastUpdated = lastUpdated; }

    
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
