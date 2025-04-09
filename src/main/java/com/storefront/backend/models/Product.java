package com.storefront.backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products")
public class Product {
    @Id
    private String id;
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
    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQty() {
        return qty;
    }

    // Setter Functions
    public void setId(String id) {
        this.id = id;
    }

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
