package com.storefront.backend.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.storefront.backend.models.Product;
import com.storefront.backend.models.User;
import com.storefront.backend.repositories.ProductRepository;
import com.storefront.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
class StorefrontIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        testUser = new User("integrationUser", "password123");
        testProduct = new Product("Electronics", "Integration Test Laptop", 1200.00, 5);
        testProduct.setId("integration-product-123");
    }

    @Test
    void completeUserWorkflow_RegisterAndLogin() throws Exception {
        // Test user registration
        doNothing().when(userRepository).save(any(User.class));

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk());

        // Test user login (mocking the repository response)
        User savedUser = new User("integrationUser", "encodedPassword");
        when(userRepository.findAll()).thenReturn(Arrays.asList(savedUser));

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk());
    }

    @Test
    void completeProductWorkflow_CreateReadUpdateDelete() throws Exception {
        // Create product
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Integration Test Laptop"));

        // Read product by ID
        when(productRepository.findById("integration-product-123")).thenReturn(Optional.of(testProduct));

        mockMvc.perform(get("/api/products/integration-product-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("integration-product-123"));

        // Read all products
        when(productRepository.findAll()).thenReturn(Arrays.asList(testProduct));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        // Update product
        Product updatedProduct = new Product("Electronics", "Updated Laptop", 1500.00, 3);
        updatedProduct.setId("integration-product-123");
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        mockMvc.perform(put("/api/products/integration-product-123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Laptop"))
                .andExpect(jsonPath("$.price").value(1500.00));

        // Delete product
        doNothing().when(productRepository).deleteById("integration-product-123");

        mockMvc.perform(delete("/api/products/integration-product-123"))
                .andExpect(status().isOk());

        verify(productRepository).deleteById("integration-product-123");
    }

    @Test
    void errorHandling_InvalidUserLogin() throws Exception {
        // Mock empty user repository (no users found)
        when(userRepository.findAll()).thenReturn(Arrays.asList());

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void errorHandling_ProductNotFound() throws Exception {
        // Mock product not found
        when(productRepository.findById("nonexistent-product")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/products/nonexistent-product"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}
