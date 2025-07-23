package com.example.logging;

/**
 * An abstraction layer for logging events, potentially to Kafka/EventHub.
 */
public interface EventLogger {
    /**
     * Logs an informational event.
     * @param event The event message or object to log.
     */
    void log(String event);
}