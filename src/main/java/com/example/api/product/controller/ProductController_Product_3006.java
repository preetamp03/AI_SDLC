package com.example.api.product.controller;

import com.example.api.product.model.ProductResponse_Product_3002;
import com.example.api.product.service.ProductService_Product_3005;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

/**
 * REST controller for product-related endpoints.
 */
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Validated
public class ProductController_Product_3006 {

    private final ProductService_Product_3005 productService;

    /**
     * Handles retrieving details for a specific product.
     * @param productId The UUID of the product.
     * @param acceptLanguage The desired language for the description.
     * @return A response entity with the product's details.
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse_Product_3002> getProductDetails(
        @PathVariable UUID productId,
        @RequestHeader(value = "Accept-Language", defaultValue = "en-US", required = false)
        @Pattern(regexp = "^[a-z]{2}(-[A-Z]{2})?$", message = "Accept-Language must be a valid IETF language tag.")
        String acceptLanguage) {

        ProductResponse_Product_3002 product = productService.getProductDetails(productId, acceptLanguage);
        return ResponseEntity.ok(product);
    }
}
```
```java
// src/main/java/com/example/api/order/model/LineItem_Order_4001.java