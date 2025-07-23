package com.example.api.product.controller;

import com.example.api.product.model.Price_Product_3001;
import com.example.api.product.model.ProductResponse_Product_3002;
import com.example.api.product.service.ProductService_Product_3005;
import com.example.common.exception.GlobalExceptionHandler_Common_1007;
import com.example.common.exception.ResourceNotFoundException_Common_1005;
import com.example.common.logging.Log4j2StructuredLogger_API_1002;
import com.example.common.model.ErrorCode_Common_1004;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for ProductController_Product_3006.
 */
@WebMvcTest(ProductController_Product_3006.class)
@Import({GlobalExceptionHandler_Common_1007.class, Log4j2StructuredLogger_API_1002.class})
class ProductController_Product_3006Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService_Product_3005 productService;

    private ProductResponse_Product_3002 productResponse;
    private UUID productId;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        productResponse = ProductResponse_Product_3002.builder()
            .id(productId)
            .name("Test Product")
            .description("A great product")
            .sku("TP-001")
            .price(Price_Product_3001.builder().amount(new BigDecimal("99.99")).currency("USD").build())
            .inventoryCount(100)
            .build();
    }

    /**
     * Tests successful retrieval of a product (200 OK).
     */
    @Test
    void getProductDetails_whenProductExists_shouldReturn200OK() throws Exception {
        when(productService.getProductDetails(eq(productId), any(String.class))).thenReturn(productResponse);

        mockMvc.perform(get("/api/v1/products/{productId}", productId)
                .header("Accept-Language", "en-US"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(productId.toString()))
            .andExpect(jsonPath("$.name").value("Test Product"));
    }

    /**
     * Tests retrieving a product that does not exist (404 Not Found).
     */
    @Test
    void getProductDetails_whenProductNotFound_shouldReturn404NotFound() throws Exception {
        when(productService.getProductDetails(eq(productId), any(String.class)))
            .thenThrow(new ResourceNotFoundException_Common_1005("Product not found", ErrorCode_Common_1004.PRODUCT_NOT_FOUND));

        mockMvc.perform(get("/api/v1/products/{productId}", productId))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.errorCode").value("PRODUCT_NOT_FOUND"));
    }

    /**
     * Tests retrieving a product with an invalid UUID format (400 Bad Request).
     */
    @Test
    void getProductDetails_whenInvalidUUID_shouldReturn400BadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/products/invalid-uuid"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorCode").value("INVALID_PRODUCT_ID"));
    }

    /**
     * Tests retrieving a product with an invalid header format (400 Bad Request).
     */
    @Test
    void getProductDetails_whenInvalidHeader_shouldReturn400BadRequest() throws Exception {
        mockMvc.perform(get("/api/v1/products/{productId}", productId)
                .header("Accept-Language", "invalid-lang-code"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorCode").value("INVALID_INPUT"));
    }
}
```
```java
// src/test/java/com/example/api/product/service/ProductService_Product_3005Test.java