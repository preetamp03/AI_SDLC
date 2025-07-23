package com.example.api.product.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 * JPA entity representing a product in the database.
 */
@Entity
@Table(name = "products")
@Data
public class ProductEntity_Product_3003 {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @ElementCollection
    @MapKeyColumn(name = "language_tag")
    @Column(name = "description", length = 2048)
    @CollectionTable(name = "product_descriptions", joinColumns = @JoinColumn(name = "product_id"))
    private Map<String, String> localizedDescriptions;

    @Column(nullable = false, unique = true)
    private String sku;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal priceAmount;

    @Column(nullable = false, length = 3)
    private String priceCurrency;

    @Column(nullable = false)
    private Integer inventoryCount;

    @ElementCollection
    @MapKeyColumn(name = "attribute_key")
    @Column(name = "attribute_value")
    @CollectionTable(name = "product_attributes", joinColumns = @JoinColumn(name = "product_id"))
    private Map<String, String> attributes;
    
    @Version
    private Long version; // For optimistic locking
}
```
```java
// src/main/java/com/example/api/product/repository/ProductRepository_Product_3004.java