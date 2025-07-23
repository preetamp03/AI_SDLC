package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Main entry point for the Chat API Spring Boot application.
 */
@SpringBootApplication
@EnableAspectJAutoProxy
public class Application {

    /**
     * The main method that starts the Spring Boot application.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        // Set system properties for log4j2 before application starts
        System.setProperty("logging.event.kafka.topic", "chat-api-events");
        System.setProperty("spring.kafka.bootstrap-servers", "localhost:9092");

        SpringApplication.run(Application.class, args);
    }
}
```
```java
// src/main/java/com/example/model/User.java