package com.example.api.order.service;

import com.example.api.order.exception.OrderValidationException_Order_4008;
import com.example.api.order.model.CreateOrderRequest_Order_4002;
import com.example.api.order.model.LineItem_Order_4001;
import com.example.api.order.model.OrderEntity_Order_4006;
import com.example.api.order.model.OrderResponse_Order_4004;
import com.example.api.order.repository.OrderRepository_Order_4007;
import com.example.api.product.model.ProductEntity_Product_3003;
import com.example.api.product.repository.ProductRepository_Product_3004;
import com.example.api.user.repository.UserRepository_User_2004;
import com.example.common.logging.StructuredLogger_API_1001;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for OrderService_Order_4009.
 */
@ExtendWith(MockitoExtension.class)
class OrderService_Order_4009Test {

    @Mock
    private OrderRepository_Order_4007 orderRepository;
    @Mock
    private ProductRepository_Product_3004 productRepository;
    @Mock
    private UserRepository_User_2004 userRepository;
    @Mock
    private StructuredLogger_API_1001 logger;

    @InjectMocks
    private OrderService_Order_4009 orderService;

    private CreateOrderRequest_Order_4002 request;
    private ProductEntity_Product_3003 product;
    private UUID customerId;
    private UUID productId;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        productId = UUID.randomUUID();

        LineItem_Order_4001 lineItem = new LineItem_Order_4001();
        lineItem.setProductId(productId);
        lineItem.setQuantity(2);

        request = new CreateOrderRequest_Order_4002();
        request.setCustomerId(customerId);
        request.setShippingAddressId(UUID.randomUUID());
        request.setLineItems(Map.of("item-1", lineItem));

        product = new ProductEntity_Product_3003();
        product.setId(productId);
        product.setInventoryCount(10);
        product.setPriceAmount(new BigDecimal("50.00"));
        product.setPriceCurrency("USD");

        when(orderRepository.save(any(OrderEntity_Order_4006.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    /**
     * Tests a successful order submission.
     */
    @Test
    void submitOrder_whenValid_shouldCreateOrderAndDecrementStock() {
        when(userRepository.existsById(customerId)).thenReturn(true);
        when(productRepository.findWithLockingById(productId)).thenReturn(Optional.of(product));
        
        OrderResponse_Order_4004 response = orderService.submitOrder(request);

        assertNotNull(response);
        assertEquals(new BigDecimal("100.00"), response.getTotalAmount());
        assertEquals(8, product.getInventoryCount());
        verify(productRepository, times(1)).saveAll(any());
        verify(orderRepository, times(1)).save(any(OrderEntity_Order_4006.class));
    }

    /**
     * Tests order submission when the customer is not found.
     */
    @Test
    void submitOrder_whenCustomerNotFound_shouldThrowException() {
        when(userRepository.existsById(customerId)).thenReturn(false);

        OrderValidationException_Order_4008 ex = assertThrows(OrderValidationException_Order_4008.class, () -> {
            orderService.submitOrder(request);
        });

        assertTrue(ex.getDetails().containsKey("customerId"));
    }

    /**
     * Tests order submission when a product is not found.
     */
    @Test
    void submitOrder_whenProductNotFound_shouldThrowException() {
        when(userRepository.existsById(customerId)).thenReturn(true);
        when(productRepository.findWithLockingById(productId)).thenReturn(Optional.empty());

        OrderValidationException_Order_4008 ex = assertThrows(OrderValidationException_Order_4008.class, () -> {
            orderService.submitOrder(request);
        });

        assertTrue(ex.getDetails().containsKey("item-1.productId"));
    }

    /**
     * Tests order submission with insufficient stock for a product.
     */
    @Test
    void submitOrder_whenInsufficientStock_shouldThrowException() {
        product.setInventoryCount(1); // Not enough for quantity of 2
        when(userRepository.existsById(customerId)).thenReturn(true);
        when(productRepository.findWithLockingById(productId)).thenReturn(Optional.of(product));

        OrderValidationException_Order_4008 ex = assertThrows(OrderValidationException_Order_4008.class, () -> {
            orderService.submitOrder(request);
        });
        
        assertTrue(ex.getDetails().containsKey("item-1.quantity"));
        assertTrue(ex.getDetails().get("item-1.quantity").contains("Insufficient stock"));
    }
}
```