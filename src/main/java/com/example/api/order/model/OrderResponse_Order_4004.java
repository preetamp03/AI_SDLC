package com.example.api.order.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * DTO for the order details returned by the API after creation.
 */
@Data
@Builder
public class OrderResponse_Order_4004 {
    private UUID id;
    private OrderStatus_Order_4003 status;
    private BigDecimal totalAmount;
    private String currency;
    private Instant createdAt;
}
```
```java
// src/main/java/com/example/api/order/model/OrderLineItemEntity_Order_4005.java