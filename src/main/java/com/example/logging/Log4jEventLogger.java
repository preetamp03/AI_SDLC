package com.example.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * A simple implementation of EventLogger that writes to Log4j2.
 */
@Component
public class Log4jEventLogger implements EventLogger {

    private static final Logger logger = LogManager.getLogger("EventLogger");

    /**
     * Logs an event using Log4j2.
     * @param event The event message to log.
     */
    @Override
    public void log(String event) {
        logger.info(event);
    }
}