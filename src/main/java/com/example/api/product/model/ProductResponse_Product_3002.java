package com.example.api.product.model;

import lombok.Builder;
import lombok.Data;
import java.util.Map;
import java.util.UUID;

/**
 * DTO for the product details returned by the API.
 */
@Data
@Builder
public class ProductResponse_Product_3002 {
    private UUID id;
    private String name;
    private String description;
    private String sku;
    private Price_Product_3001 price;
    private Integer inventoryCount;
    private Map<String, String> attributes;
}
```
```java
// src/main/java/com/example/api/product/model/ProductEntity_Product_3003.java