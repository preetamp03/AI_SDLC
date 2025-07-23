package com.example.logging;

/**
 * Interface for an abstracted event logger to send structured logs
 * to a system like Kafka or EventHub.
 */
public interface EventLogger {

    /**
     * Logs an event.
     * @param eventName The name of the event.
     * @param eventDetails The details or payload of the event as a JSON string.
     */
    void logEvent(String eventName, String eventDetails);
}