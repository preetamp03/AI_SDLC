package com.example.api.product.service;

import com.example.api.product.model.Price_Product_3001;
import com.example.api.product.model.ProductEntity_Product_3003;
import com.example.api.product.model.ProductResponse_Product_3002;
import com.example.api.product.repository.ProductRepository_Product_3004;
import com.example.common.exception.ResourceNotFoundException_Common_1005;
import com.example.common.logging.StructuredLogger_API_1001;
import com.example.common.model.ErrorCode_Common_1004;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Service layer containing business logic for product management.
 */
@Service
@RequiredArgsConstructor
public class ProductService_Product_3005 {

    private final ProductRepository_Product_3004 productRepository;
    private final StructuredLogger_API_1001 logger;
    private static final String DEFAULT_LANGUAGE = "en-US";

    /**
     * Retrieves details for a specific product.
     * @param productId The UUID of the product.
     * @param languageTag The desired language for the description.
     * @return A DTO with the product's details.
     */
    @Transactional(readOnly = true)
    public ProductResponse_Product_3002 getProductDetails(UUID productId, String languageTag) {
        final long startTime = System.currentTimeMillis();
        final String methodName = "getProductDetails";
        logger.logStart(this.getClass().getSimpleName(), methodName);

        ProductEntity_Product_3003 product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException_Common_1005(
                "A product with the specified ID could not be found.",
                ErrorCode_Common_1004.PRODUCT_NOT_FOUND
            ));

        String description = product.getLocalizedDescriptions().getOrDefault(languageTag,
            product.getLocalizedDescriptions().get(DEFAULT_LANGUAGE));

        ProductResponse_Product_3002 response = ProductResponse_Product_3002.builder()
            .id(product.getId())
            .name(product.getName())
            .description(description)
            .sku(product.getSku())
            .price(Price_Product_3001.builder()
                .amount(product.getPriceAmount())
                .currency(product.getPriceCurrency())
                .build())
            .inventoryCount(product.getInventoryCount())
            .attributes(product.getAttributes())
            .build();

        logger.logEnd(this.getClass().getSimpleName(), methodName, startTime);
        return response;
    }
}
```
```java
// src/main/java/com/example/api/product/controller/ProductController_Product_3006.java