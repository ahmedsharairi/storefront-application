package com.storefront.backend;

public class Product {
    private String category;
    private String name;
    private double price;
    private int qty;

    // Allows for empty item
    public Product() {}

    // Constructor
    public Product(String category, String name, double price, int qty) {
        this.category = category;
        this.name = name;
        this.price = price;
        this.qty = qty;
    }

    // Getter Functions
    public String getCategory() {
        return this.category;
    }

    public String getName() {
        return this.name;
    }

    public double getPrice() {
        return this.price;
    }

    public int getQty() {
        return this.qty;
    }

    // Setter Functions

    public void setCategory(String category) {
        this.category = category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

}
