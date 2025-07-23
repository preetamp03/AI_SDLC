package com.example.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.example.repository.IUserRepository;

@SpringBootTest(classes = ApplicationConfig.class)
class ApplicationConfigTest {

    @MockBean
    private IUserRepository userRepository;

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
// src/test/java/com/example/controller/AuthControllerTest.java