package com.storefront.backend.services;

import com.storefront.backend.models.Product;
import com.storefront.backend.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;
    private Product savedProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product("Electronics", "Laptop", 999.99, 10);
        savedProduct = new Product("Electronics", "Laptop", 999.99, 10);
        savedProduct.setId("product123");
    }

    @Test
    void saveProduct_Success() {
        // Arrange
        when(productRepository.save(testProduct)).thenReturn(savedProduct);

        // Act
        Product result = productService.saveProduct(testProduct);

        // Assert
        assertNotNull(result);
        assertEquals("product123", result.getId());
        assertEquals("Electronics", result.getCategory());
        assertEquals("Laptop", result.getName());
        assertEquals(999.99, result.getPrice());
        assertEquals(10, result.getQty());
        verify(productRepository).save(testProduct);
    }

    @Test
    void getProductById_Success_ReturnsProduct() {
        // Arrange
        when(productRepository.findById("product123")).thenReturn(Optional.of(savedProduct));

        // Act
        Optional<Product> result = productService.getProductById("product123");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("product123", result.get().getId());
        assertEquals("Laptop", result.get().getName());
        verify(productRepository).findById("product123");
    }

    @Test
    void getProductById_NotFound_ReturnsEmpty() {
        // Arrange
        when(productRepository.findById("nonexistent")).thenReturn(Optional.empty());

        // Act
        Optional<Product> result = productService.getProductById("nonexistent");

        // Assert
        assertFalse(result.isPresent());
        verify(productRepository).findById("nonexistent");
    }

    @Test
    void getAllProducts_Success_ReturnsProductList() {
        // Arrange
        Product product2 = new Product("Books", "Java Guide", 29.99, 50);
        product2.setId("product456");
        List<Product> products = Arrays.asList(savedProduct, product2);
        when(productRepository.findAll()).thenReturn(products);

        // Act
        List<Product> result = productService.getAllProducts();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Laptop", result.get(0).getName());
        assertEquals("Java Guide", result.get(1).getName());
        verify(productRepository).findAll();
    }

    @Test
    void getAllProducts_EmptyList_ReturnsEmptyList() {
        // Arrange
        when(productRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Product> result = productService.getAllProducts();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepository).findAll();
    }

    @Test
    void deleteProduct_Success() {
        // Arrange
        doNothing().when(productRepository).deleteById("product123");

        // Act
        productService.deleteProduct("product123");

        // Assert
        verify(productRepository).deleteById("product123");
    }
}
