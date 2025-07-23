package com.example.api.order.controller;

import com.example.api.order.exception.OrderValidationException_Order_4008;
import com.example.api.order.model.*;
import com.example.api.order.service.OrderService_Order_4009;
import com.example.common.exception.GlobalExceptionHandler_Common_1007;
import com.example.common.logging.Log4j2StructuredLogger_API_1002;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for OrderController_Order_4010.
 */
@WebMvcTest(OrderController_Order_4010.class)
@Import({GlobalExceptionHandler_Common_1007.class, Log4j2StructuredLogger_API_1002.class})
class OrderController_Order_4010Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService_Order_4009 orderService;

    private CreateOrderRequest_Order_4002 validRequest;
    private OrderResponse_Order_4004 orderResponse;

    @BeforeEach
    void setUp() {
        LineItem_Order_4001 lineItem = new LineItem_Order_4001();
        lineItem.setProductId(UUID.randomUUID());
        lineItem.setQuantity(2);

        validRequest = new CreateOrderRequest_Order_4002();
        validRequest.setCustomerId(UUID.randomUUID());
        validRequest.setShippingAddressId(UUID.randomUUID());
        validRequest.setLineItems(Map.of("item-1", lineItem));

        orderResponse = OrderResponse_Order_4004.builder()
            .id(UUID.randomUUID())
            .status(OrderStatus_Order_4003.ACCEPTED)
            .totalAmount(new BigDecimal("199.98"))
            .currency("USD")
            .createdAt(Instant.now())
            .build();
    }

    /**
     * Tests successful order submission (202 Accepted).
     */
    @Test
    @WithMockUser
    void submitOrder_whenValidRequest_shouldReturn202Accepted() throws Exception {
        when(orderService.submitOrder(any(CreateOrderRequest_Order_4002.class))).thenReturn(orderResponse);

        mockMvc.perform(post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
            .andExpect(status().isAccepted())
            .andExpect(jsonPath("$.id").value(orderResponse.getId().toString()))
            .andExpect(jsonPath("$.status").value("ACCEPTED"));
    }

    /**
     * Tests order submission with an empty line item list (400 Bad Request).
     */
    @Test
    @WithMockUser
    void submitOrder_whenEmptyLineItems_shouldReturn400BadRequest() throws Exception {
        validRequest.setLineItems(Map.of());

        mockMvc.perform(post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errorCode").value("INVALID_INPUT"))
            .andExpect(jsonPath("$.invalidFields.lineItems").value("Must contain at least one line item."));
    }
    
    /**
     * Tests order submission without authentication (401 Unauthorized).
     */
    @Test
    void submitOrder_whenNotAuthenticated_shouldReturn401Unauthorized() throws Exception {
         mockMvc.perform(post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
            .andExpect(status().isUnauthorized());
    }

    /**
     * Tests order submission that fails business validation (422 Unprocessable Entity).
     */
    @Test
    @WithMockUser
    void submitOrder_whenValidationFails_shouldReturn422UnprocessableEntity() throws Exception {
        Map<String, String> details = Map.of("item-1.quantity", "Insufficient stock");
        when(orderService.submitOrder(any(CreateOrderRequest_Order_4002.class)))
            .thenThrow(new OrderValidationException_Order_4008("Order validation failed", details));

        mockMvc.perform(post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.errorCode").value("ORDER_VALIDATION_FAILED"))
            .andExpect(jsonPath("$.details.['item-1.quantity']").value("Insufficient stock"));
    }
}
```
```java
// src/test/java/com/example/api/order/service/OrderService_Order_4009Test.java