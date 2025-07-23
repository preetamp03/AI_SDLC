package com.example.api.order.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

/**
 * DTO for the create order request payload.
 */
@Data
public class CreateOrderRequest_Order_4002 {

    @NotNull(message = "Customer ID is required.")
    private UUID customerId;

    @NotNull(message = "Line items cannot be null.")
    @NotEmpty(message = "Must contain at least one line item.")
    private Map<String, @Valid LineItem_Order_4001> lineItems;

    @NotNull(message = "Shipping address ID is required.")
    private UUID shippingAddressId;
}
```
```java
// src/main/java/com/example/api/order/model/OrderStatus_Order_4003.java