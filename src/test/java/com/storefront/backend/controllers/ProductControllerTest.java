package com.storefront.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.storefront.backend.models.Product;
import com.storefront.backend.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private Product testProduct;
    private Product savedProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product("Electronics", "Laptop", 999.99, 10);
        savedProduct = new Product("Electronics", "Laptop", 999.99, 10);
        savedProduct.setId("product123");
    }

    @Test
    void createProduct_Success() throws Exception {
        // Arrange
        when(productService.saveProduct(any(Product.class))).thenReturn(savedProduct);

        // Act & Assert
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("product123"))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.category").value("Electronics"))
                .andExpect(jsonPath("$.price").value(999.99))
                .andExpect(jsonPath("$.qty").value(10));

        verify(productService).saveProduct(any(Product.class));
    }

    @Test
    void getProductById_Success_ReturnsProduct() throws Exception {
        // Arrange
        when(productService.getProductById("product123")).thenReturn(Optional.of(savedProduct));

        // Act & Assert
        mockMvc.perform(get("/api/products/product123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("product123"))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.category").value("Electronics"));

        verify(productService).getProductById("product123");
    }

    @Test
    void getProductById_NotFound_ReturnsEmpty() throws Exception {
        // Arrange
        when(productService.getProductById("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/products/nonexistent"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(productService).getProductById("nonexistent");
    }

    @Test
    void getAllProducts_Success_ReturnsProductList() throws Exception {
        // Arrange
        Product product2 = new Product("Books", "Java Guide", 29.99, 50);
        product2.setId("product456");
        List<Product> products = Arrays.asList(savedProduct, product2);
        when(productService.getAllProducts()).thenReturn(products);

        // Act & Assert
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[1].name").value("Java Guide"));

        verify(productService).getAllProducts();
    }

    @Test
    void getAllProducts_EmptyList_ReturnsEmptyArray() throws Exception {
        // Arrange
        when(productService.getAllProducts()).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(productService).getAllProducts();
    }

    @Test
    void updateProduct_Success() throws Exception {
        // Arrange
        Product updatedProduct = new Product("Electronics", "Gaming Laptop", 1299.99, 5);
        updatedProduct.setId("product123");
        when(productService.saveProduct(any(Product.class))).thenReturn(updatedProduct);

        // Act & Assert
        mockMvc.perform(put("/api/products/product123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("product123"))
                .andExpect(jsonPath("$.name").value("Gaming Laptop"))
                .andExpect(jsonPath("$.price").value(1299.99));

        verify(productService).saveProduct(any(Product.class));
    }

    @Test
    void deleteProduct_Success() throws Exception {
        // Arrange
        doNothing().when(productService).deleteProduct("product123");

        // Act & Assert
        mockMvc.perform(delete("/api/products/product123"))
                .andExpect(status().isOk());

        verify(productService).deleteProduct("product123");
    }
}
