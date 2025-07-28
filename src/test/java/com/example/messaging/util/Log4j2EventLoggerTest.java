package com.example.messaging.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class Log4j2EventLoggerTest {

    private final Log4j2EventLogger logger = new Log4j2EventLogger();

    /**
     * Tests that logging a simple event does not throw an exception.
     */
    @Test
    void logEvent_shouldNotThrowException() {
        assertDoesNotThrow(() -> logger.logEvent("TestEvent", "Some details here."));
    }

    /**
     * Tests that logging an event with duration does not throw an exception.
     */
    @Test
    void logEventWithDuration_shouldNotThrowException() {
        assertDoesNotThrow(() -> logger.logEvent("TestEventWithDuration", "Details with time.", 123L));
    }
}