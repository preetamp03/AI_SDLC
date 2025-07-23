package com.example.logging;

import java.util.Map;

public interface EventLogger {
    /**
     * Logs a structured event.
     * @param eventName The name of the event.
     * @param properties A map of key-value pairs describing the event.
     */
    void logEvent(String eventName, Map<String, Object> properties);
}
```
```java
// src/main/java/com/example/logging/KafkaEventLogger.java