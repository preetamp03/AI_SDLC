package com.example.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = OpenApiConfig.class)
class OpenApiConfigTest {

    /**
     * Test to ensure the configuration class can be loaded into the context.
     */
    @Test
    void contextLoads() {
        // This test simply verifies that the Spring context can load with this configuration bean.
    }
}
```
```java
// src/test/java/com/example/config/SecurityConfigTest.java