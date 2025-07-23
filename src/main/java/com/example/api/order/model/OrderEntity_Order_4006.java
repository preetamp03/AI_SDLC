package com.example.api.order.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * JPA entity representing a customer order.
 */
@Entity
@Table(name = "orders")
@Data
public class OrderEntity_Order_4006 {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID customerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus_Order_4003 status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderLineItemEntity_Order_4005> lineItems = new HashSet<>();

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Column(nullable = false, length = 3)
    private String currency;
    
    @Column(nullable = false)
    private UUID shippingAddressId;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
}
```
```java
// src/main/java/com/example/api/order/repository/OrderRepository_Order_4007.java