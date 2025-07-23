package com.example.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KafkaEventLogger implements EventLogger {

    private static final Logger eventLogger = LogManager.getLogger("EventLogger");

    /**
     * Logs a structured event message. The underlying log4j2 configuration
     * will route this to a Kafka appender.
     * @param eventName The name of the event (e.g., "UserLogin").
     * @param eventDetails A JSON string representing the event payload.
     */
    @Override
    public void logEvent(String eventName, String eventDetails) {
        // Construct a structured log message, e.g., in JSON format
        String logMessage = String.format("{\"event\": \"%s\", \"details\": %s}", eventName, eventDetails);
        eventLogger.info(logMessage);
    }
}
```
```java
// src/main/java/com/example/logging/LogExecutionTime.java