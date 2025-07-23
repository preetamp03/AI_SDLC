package com.example.api.order.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * JPA entity representing a line item within an order.
 */
@Entity
@Table(name = "order_line_items")
@Data
@EqualsAndHashCode(exclude = "order")
public class OrderLineItemEntity_Order_4005 {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity_Order_4006 order;

    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal priceAtTimeOfOrder;
}
```
```java
// src/main/java/com/example/api/order/model/OrderEntity_Order_4006.java