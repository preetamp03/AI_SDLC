package com.example.api.order.service;

import com.example.api.order.exception.OrderValidationException_Order_4008;
import com.example.api.order.model.*;
import com.example.api.order.repository.OrderRepository_Order_4007;
import com.example.api.product.model.ProductEntity_Product_3003;
import com.example.api.product.repository.ProductRepository_Product_3004;
import com.example.api.user.repository.UserRepository_User_2004;
import com.example.common.logging.StructuredLogger_API_1001;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service layer containing business logic for order management.
 */
@Service
@RequiredArgsConstructor
public class OrderService_Order_4009 {

    private final OrderRepository_Order_4007 orderRepository;
    private final ProductRepository_Product_3004 productRepository;
    private final UserRepository_User_2004 userRepository;
    private final StructuredLogger_API_1001 logger;

    /**
     * Submits a new customer order for processing.
     * @param request The DTO containing order details.
     * @return A DTO with the created order's initial state.
     */
    @Transactional
    public OrderResponse_Order_4004 submitOrder(CreateOrderRequest_Order_4002 request) {
        final long startTime = System.currentTimeMillis();
        final String methodName = "submitOrder";
        logger.logStart(this.getClass().getSimpleName(), methodName);

        validateOrderRequest(request);

        Map<UUID, ProductEntity_Product_3003> productsToUpdate = new HashMap<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        String currency = null;
        
        OrderEntity_Order_4006 newOrder = new OrderEntity_Order_4006();

        for (Map.Entry<String, LineItem_Order_4001> entry : request.getLineItems().entrySet()) {
            LineItem_Order_4001 item = entry.getValue();
            UUID productId = item.getProductId();
            
            ProductEntity_Product_3003 product = productRepository.findWithLockingById(productId)
                    .orElseThrow(() -> new OrderValidationException_Order_4008(
                            "Order validation failed",
                            Map.of(entry.getKey() + ".productId", "Product not found")));

            if (product.getInventoryCount() < item.getQuantity()) {
                throw new OrderValidationException_Order_4008(
                        "Order validation failed",
                        Map.of(entry.getKey() + ".quantity", "Insufficient stock for product " + product.getName()));
            }
            
            product.setInventoryCount(product.getInventoryCount() - item.getQuantity());
            productsToUpdate.put(productId, product);
            
            BigDecimal lineItemTotal = product.getPriceAmount().multiply(BigDecimal.valueOf(item.getQuantity()));
            totalAmount = totalAmount.add(lineItemTotal);
            if (currency == null) {
                currency = product.getPriceCurrency();
            }
            
            OrderLineItemEntity_Order_4005 lineEntity = new OrderLineItemEntity_Order_4005();
            lineEntity.setProductId(productId);
            lineEntity.setQuantity(item.getQuantity());
            lineEntity.setPriceAtTimeOfOrder(product.getPriceAmount());
            lineEntity.setOrder(newOrder);
            newOrder.getLineItems().add(lineEntity);
        }

        productRepository.saveAll(productsToUpdate.values());

        newOrder.setCustomerId(request.getCustomerId());
        newOrder.setShippingAddressId(request.getShippingAddressId());
        newOrder.setStatus(OrderStatus_Order_4003.ACCEPTED);
        newOrder.setTotalAmount(totalAmount);
        newOrder.setCurrency(currency);

        OrderEntity_Order_4006 savedOrder = orderRepository.save(newOrder);

        logger.logInfo("Order accepted successfully", "orderId", savedOrder.getId());

        OrderResponse_Order_4004 response = OrderResponse_Order_4004.builder()
                .id(savedOrder.getId())
                .status(savedOrder.getStatus())
                .totalAmount(savedOrder.getTotalAmount())
                .currency(savedOrder.getCurrency())
                .createdAt(savedOrder.getCreatedAt())
                .build();
        
        logger.logEnd(this.getClass().getSimpleName(), methodName, startTime);
        return response;
    }

    /**
     * Performs initial validation of the order request data.
     * @param request The order creation request.
     */
    private void validateOrderRequest(CreateOrderRequest_Order_4002 request) {
        Map<String, String> errors = new HashMap<>();

        if (!userRepository.existsById(request.getCustomerId())) {
            errors.put("customerId", "The specified customer does not exist.");
        }
        // Mock validation for shipping address
        // In a real app, you would check if the address exists and belongs to the customer.
        if (request.getShippingAddressId() == null) {
             errors.put("shippingAddressId", "The shipping address ID is invalid.");
        }

        if (!errors.isEmpty()) {
            throw new OrderValidationException_Order_4008("Order validation failed", errors);
        }
    }
}
```
```java
// src/main/java/com/example/api/order/controller/OrderController_Order_4010.java