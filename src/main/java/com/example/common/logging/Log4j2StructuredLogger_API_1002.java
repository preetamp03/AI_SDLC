package com.example.common.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Log4j2 implementation of the structured logging interface.
 */
@Component
public class Log4j2StructuredLogger_API_1002 implements StructuredLogger_API_1001 {

    private static final Logger logger = LogManager.getLogger(Log4j2StructuredLogger_API_1002.class);

    @Override
    public void logStart(String className, String methodName) {
        logger.info("START: {}.{}()", className, methodName);
    }

    @Override
    public void logEnd(String className, String methodName, long startTimeMillis) {
        long duration = System.currentTimeMillis() - startTimeMillis;
        logger.info("END: {}.{}() - Duration: {}ms", className, methodName, duration);
    }

    @Override
    public void logInfo(String message, Object... details) {
        logger.info(message, details);
    }

    @Override
    public void logError(String message, Throwable throwable, Object... details) {
        logger.error(message, details, throwable);
    }
}
```
```java
// src/main/java/com/example/common/model/ErrorResponse_Common_1003.java