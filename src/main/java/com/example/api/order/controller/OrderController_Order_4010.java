package com.example.api.order.controller;

import com.example.api.order.model.CreateOrderRequest_Order_4002;
import com.example.api.order.model.OrderResponse_Order_4004;
import com.example.api.order.service.OrderService_Order_4009;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for order-related endpoints.
 */
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController_Order_4010 {

    private final OrderService_Order_4009 orderService;

    /**
     * Handles the submission of a new customer order.
     * @param createOrderRequest The request body containing order details.
     * @return A response entity with the created order's data and a 202 status.
     */
    @PostMapping
    public ResponseEntity<OrderResponse_Order_4004> submitOrder(@Valid @RequestBody CreateOrderRequest_Order_4002 createOrderRequest) {
        OrderResponse_Order_4004 order = orderService.submitOrder(createOrderRequest);
        return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
    }
}
```
```java
// src/main/java/com/example/common/exception/GlobalExceptionHandler_Common_1007.java