package com.storefront.backend.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
    }

    @Test
    void testDefaultConstructor() {
        // Act & Assert
        assertNotNull(product);
        assertNull(product.getId());
        assertNull(product.getCategory());
        assertNull(product.getName());
        assertEquals(0.0, product.getPrice());
        assertEquals(0, product.getQty());
    }

    @Test
    void testParameterizedConstructor() {
        // Act
        Product product = new Product("Electronics", "Laptop", 999.99, 10);

        // Assert
        assertNotNull(product);
        assertEquals("Electronics", product.getCategory());
        assertEquals("Laptop", product.getName());
        assertEquals(999.99, product.getPrice());
        assertEquals(10, product.getQty());
        assertNull(product.getId()); // ID should be null as it's set by MongoDB
    }

    @Test
    void testAllSettersAndGetters() {
        // Act
        product.setId("product123");
        product.setCategory("Books");
        product.setName("Java Programming Guide");
        product.setPrice(29.99);
        product.setQty(50);

        // Assert
        assertEquals("product123", product.getId());
        assertEquals("Books", product.getCategory());
        assertEquals("Java Programming Guide", product.getName());
        assertEquals(29.99, product.getPrice());
        assertEquals(50, product.getQty());
    }

    @Test
    void testCategorySetterAndGetter() {
        // Act
        product.setCategory("Clothing");

        // Assert
        assertEquals("Clothing", product.getCategory());
    }

    @Test
    void testNameSetterAndGetter() {
        // Act
        product.setName("T-Shirt");

        // Assert
        assertEquals("T-Shirt", product.getName());
    }

    @Test
    void testPriceSetterAndGetter() {
        // Act
        product.setPrice(19.99);

        // Assert
        assertEquals(19.99, product.getPrice());
    }

    @Test
    void testQtySetterAndGetter() {
        // Act
        product.setQty(100);

        // Assert
        assertEquals(100, product.getQty());
    }

    @Test
    void testIdSetterAndGetter() {
        // Act
        product.setId("uniqueProductId");

        // Assert
        assertEquals("uniqueProductId", product.getId());
    }

    @Test
    void testProductWithZeroPrice() {
        // Act
        product.setPrice(0.0);

        // Assert
        assertEquals(0.0, product.getPrice());
    }

    @Test
    void testProductWithZeroQuantity() {
        // Act
        product.setQty(0);

        // Assert
        assertEquals(0, product.getQty());
    }

    @Test
    void testProductWithNegativePrice() {
        // Act
        product.setPrice(-10.0);

        // Assert
        assertEquals(-10.0, product.getPrice());
    }

    @Test
    void testProductWithNegativeQuantity() {
        // Act
        product.setQty(-5);

        // Assert
        assertEquals(-5, product.getQty());
    }

    @Test
    void testProductWithNullValues() {
        // Act
        product.setId(null);
        product.setCategory(null);
        product.setName(null);

        // Assert
        assertNull(product.getId());
        assertNull(product.getCategory());
        assertNull(product.getName());
    }

    @Test
    void testProductWithEmptyStrings() {
        // Act
        product.setId("");
        product.setCategory("");
        product.setName("");

        // Assert
        assertEquals("", product.getId());
        assertEquals("", product.getCategory());
        assertEquals("", product.getName());
    }
}
