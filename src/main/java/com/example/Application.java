package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Spring Boot application.
 */
@SpringBootApplication
public class Application {

    /**
     * Starts the Spring Boot application.
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
```
```java
// src/main/java/com/example/common/logging/StructuredLogger_API_1001.java