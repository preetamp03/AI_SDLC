package com.example.logging;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class KafkaEventLogger implements EventLogger {

    private static final Logger log = LogManager.getLogger(KafkaEventLogger.class);
    // private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Logs an event to a Kafka topic. This is a placeholder implementation.
     * In a real app, it would use the KafkaTemplate to send the event.
     * @param eventName The name of the event (used as topic or key).
     * @param properties The event payload.
     */
    @Override
    public void logEvent(String eventName, Map<String, Object> properties) {
        // In a real implementation:
        // try {
        //     kafkaTemplate.send(eventName, properties);
        //     log.info("Logged event '{}' to Kafka.", eventName);
        // } catch (Exception e) {
        //     log.error("Failed to log event '{}' to Kafka.", eventName, e);
        // }

        // Placeholder implementation for demonstration:
        log.info("[KAFKA_EVENT] Event: {}, Properties: {}", eventName, properties.toString());
    }
}
```
```java
// src/main/java/com/example/logging/LoggingAspect.java