package com.example.api.product.service;

import com.example.api.product.model.ProductEntity_Product_3003;
import com.example.api.product.model.ProductResponse_Product_3002;
import com.example.api.product.repository.ProductRepository_Product_3004;
import com.example.common.exception.ResourceNotFoundException_Common_1005;
import com.example.common.logging.StructuredLogger_API_1001;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for ProductService_Product_3005.
 */
@ExtendWith(MockitoExtension.class)
class ProductService_Product_3005Test {

    @Mock
    private ProductRepository_Product_3004 productRepository;
    
    @Mock
    private StructuredLogger_API_1001 logger;

    @InjectMocks
    private ProductService_Product_3005 productService;

    private ProductEntity_Product_3003 productEntity;
    private UUID productId;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        productEntity = new ProductEntity_Product_3003();
        productEntity.setId(productId);
        productEntity.setName("Super Widget");
        productEntity.setLocalizedDescriptions(Map.of(
            "en-US", "A super widget.",
            "fr-FR", "Un super widget."
        ));
        productEntity.setPriceAmount(new BigDecimal("19.99"));
        productEntity.setPriceCurrency("USD");
    }

    /**
     * Tests successful retrieval of a product with a specific language.
     */
    @Test
    void getProductDetails_whenProductExistsAndLanguageExists_shouldReturnCorrectDescription() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(productEntity));

        ProductResponse_Product_3002 response = productService.getProductDetails(productId, "fr-FR");

        assertEquals("Un super widget.", response.getDescription());
        assertEquals(productId, response.getId());
    }

    /**
     * Tests retrieval of a product falling back to the default language.
     */
    @Test
    void getProductDetails_whenProductExistsAndLanguageMissing_shouldReturnDefaultDescription() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(productEntity));

        ProductResponse_Product_3002 response = productService.getProductDetails(productId, "es-ES");

        assertEquals("A super widget.", response.getDescription());
    }

    /**
     * Tests that an exception is thrown when the product is not found.
     */
    @Test
    void getProductDetails_whenProductNotFound_shouldThrowResourceNotFoundException() {
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException_Common_1005.class, () -> {
            productService.getProductDetails(productId, "en-US");
        });
    }
}
```
```java
// src/test/java/com/example/api/order/controller/OrderController_Order_4010Test.java