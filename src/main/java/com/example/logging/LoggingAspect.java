package com.example.logging;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {

    private final EventLogger eventLogger;
    private static final Logger logger = LogManager.getLogger(LoggingAspect.class);

    /**
     * Pointcut for all methods in the service and controller layers.
     */
    @Pointcut("within(com.example.service..*) || within(com.example.controller..*)")
    public void applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut definition.
    }

    /**
     * Advice that logs when a method is entered and exited, and how long it took.
     * @param joinPoint The join point for the advised method.
     * @return The result of the original method call.
     * @throws Throwable if the advised method throws an exception.
     */
    @Around("applicationPackagePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        String args = Arrays.toString(joinPoint.getArgs());
        String logMessageStart = String.format("START: %s with args=%s", methodName, args);

        logger.info(logMessageStart);
        eventLogger.log(logMessageStart);

        long startTime = System.currentTimeMillis();
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            long endTime = System.currentTimeMillis();
            long timeTaken = endTime - startTime;
            String logMessageError = String.format("ERROR: %s finished in %dms with exception: %s", methodName, timeTaken, throwable.getMessage());
            logger.error(logMessageError, throwable);
            eventLogger.log(logMessageError);
            throw throwable;
        }

        long endTime = System.currentTimeMillis();
        long timeTaken = endTime - startTime;
        String logMessageEnd = String.format("END: %s finished in %dms", methodName, timeTaken);
        logger.info(logMessageEnd);
        eventLogger.log(logMessageEnd);

        return result;
    }
}