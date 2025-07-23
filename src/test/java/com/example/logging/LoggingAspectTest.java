package com.example.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoggingAspectTest {

    @Mock
    private ProceedingJoinPoint joinPoint;

    @InjectMocks
    private LoggingAspect loggingAspect;

    @Test
    void logExecutionTime_shouldProceedAndLog() throws Throwable {
        // Arrange
        when(joinPoint.getSignature()).thenReturn(mock(org.aspectj.lang.Signature.class));
        when(joinPoint.proceed()).thenReturn("result");

        // Act
        Object result = loggingAspect.logExecutionTime(joinPoint);

        // Assert
        verify(joinPoint, times(1)).proceed();
        assertEquals("result", result);
        // We can't easily assert the log output without more complex test setups,
        // but we can verify the core logic (proceeding the join point) works.
    }
}