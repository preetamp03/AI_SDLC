package com.example.messaging.util;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class Log4j2EventLogger implements EventLogger {

    /**
     * Logs a generic event using Log4j2.
     * @param eventName The name of the event.
     * @param details Additional details about the event.
     */
    @Override
    public void logEvent(String eventName, String details) {
        log.info("EVENT: [{}], DETAILS: [{}]", eventName, details != null ? details : "N/A");
    }

    /**
     * Logs an event with its duration in milliseconds using Log4j2.
     * @param eventName The name of the event.
     * @param details Additional details about the event.
     * @param durationMs The duration of the event in milliseconds.
     */
    @Override
    public void logEvent(String eventName, String details, long durationMs) {
        log.info("EVENT: [{}], DETAILS: [{}], DURATION_MS: [{}]", eventName, details != null ? details : "N/A", durationMs);
    }
}