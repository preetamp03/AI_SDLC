package com.example.api.order.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Max;
import lombok.Data;

import java.util.UUID;

/**
 * DTO representing a single line item in an order.
 */
@Data
public class LineItem_Order_4001 {

    @NotNull(message = "Product ID is required.")
    private UUID productId;

    @NotNull(message = "Quantity is required.")
    @Min(value = 1, message = "Quantity must be at least 1.")
    @Max(value = 100, message = "Quantity cannot exceed 100.")
    private Integer quantity;
}
```
```java
// src/main/java/com/example/api/order/model/CreateOrderRequest_Order_4002.java