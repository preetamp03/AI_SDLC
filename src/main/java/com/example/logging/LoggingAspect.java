package com.example.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LogManager.getLogger(LoggingAspect.class);

    /**
     * An Around advice that logs the execution time of methods annotated
     * with @LogExecutionTime.
     * @param joinPoint The proceeding join point.
     * @return The result of the method execution.
     * @throws Throwable If the method execution throws an exception.
     */
    @Around("@annotation(com.example.logging.LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        logger.info("{} executed in {}ms", joinPoint.getSignature(), executionTime);
        return proceed;
    }
}