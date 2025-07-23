package com.example.common.logging;

/**
 * An abstraction for structured logging to support various logging backends.
 */
public interface StructuredLogger_API_1001 {

    /**
     * Logs the start of a method execution.
     * @param className The name of the class.
     * @param methodName The name of the method.
     */
    void logStart(String className, String methodName);

    /**
     * Logs the end of a method execution, including the duration.
     * @param className The name of the class.
     * @param methodName The name of the method.
     * @param startTimeMillis The start time in milliseconds.
     */
    void logEnd(String className, String methodName, long startTimeMillis);

    /**
     * Logs an informational message.
     * @param message The message to log.
     * @param details Additional key-value details for structured logging.
     */
    void logInfo(String message, Object... details);

    /**
     * Logs an error message with an exception.
     * @param message The error message.
     * @param throwable The exception to log.
     * @param details Additional key-value details.
     */
    void logError(String message, Throwable throwable, Object... details);
}
```
```java
// src/main/java/com/example/common/logging/Log4j2StructuredLogger_API_1002.java