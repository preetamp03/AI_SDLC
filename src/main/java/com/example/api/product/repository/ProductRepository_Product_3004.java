package com.example.api.product.repository;

import com.example.api.product.model.ProductEntity_Product_3003;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for ProductEntity_Product_3003.
 */
@Repository
public interface ProductRepository_Product_3004 extends JpaRepository<ProductEntity_Product_3003, UUID> {
    
    /**
     * Finds a product by its ID with a pessimistic write lock.
     * @param id The UUID of the product.
     * @return An Optional containing the locked product if found.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ProductEntity_Product_3003> findWithLockingById(UUID id);
}
```
```java
// src/main/java/com/example/api/product/service/ProductService_Product_3005.java