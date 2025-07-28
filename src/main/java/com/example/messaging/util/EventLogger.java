package com.example.messaging.util;

public interface EventLogger {
    /**
     * Logs a generic event.
     * @param eventName The name of the event.
     * @param details Additional details about the event.
     */
    void logEvent(String eventName, String details);

    /**
     * Logs an event with its duration in milliseconds.
     * @param eventName The name of the event.
     * @param details Additional details about the event.
     * @param durationMs The duration of the event in milliseconds.
     */
    void logEvent(String eventName, String details, long durationMs);
}