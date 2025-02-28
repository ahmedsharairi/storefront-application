package com.storefront.backend;

import java.util.ArrayList;

public class ProductList {
    private ArrayList<Object> products = new ArrayList<Object>();

    public void addProduct(Product product) {
        ArrayList<Object> productDetails = new ArrayList<Object>();
        productDetails.add(product.getCategory());
        productDetails.add(product.getName());
        productDetails.add(product.getPrice());
        productDetails.add(product.getQty());

        products.add(productDetails);

    }

    public void displayProducts() {
        for (Object product: products) {
            System.out.println(product);
        }
    }
}
