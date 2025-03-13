/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author C
 */
public class Item{
    private int id;
    private String itemName;
    private String itemDescription;
    private int price;
    private String image;

    public Item(int id, String itemName, String itemDescription, int price, String image) {
        this.id = id;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.price = price;
        this.image = image;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public int getPrice() {
        return price;
    }

    public int getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }
    
    
    
}
