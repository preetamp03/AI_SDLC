package com.example.api.product.model;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

/**
 * DTO representing the price of a product.
 */
@Data
@Builder
public class Price_Product_3001 {
    private BigDecimal amount;
    private String currency;
}
```
```java
// src/main/java/com/example/api/product/model/ProductResponse_Product_3002.java