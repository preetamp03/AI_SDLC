package com.example.api.order.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

/**
 * Exception thrown when an order fails business logic validation.
 */
@Getter
@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class OrderValidationException_Order_4008 extends RuntimeException {
    private final Map<String, String> details;

    public OrderValidationException_Order_4008(String message, Map<String, String> details) {
        super(message);
        this.details = details;
    }
}
```
```java
// src/main/java/com/example/api/order/service/OrderService_Order_4009.java