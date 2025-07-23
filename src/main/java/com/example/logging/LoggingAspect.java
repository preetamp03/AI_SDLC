package com.example.logging;

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
public class LoggingAspect {
    private static final Logger log = LogManager.getLogger(LoggingAspect.class);

    /**
     * Defines a pointcut for all methods in the service and controller packages.
     */
    @Pointcut("within(com.example.service..*) || within(com.example.controller..*)")
    public void applicationPackagePointcut() {
        // Method is empty as this is just a Pointcut definition.
    }

    /**
     * Logs method entry, exit, and execution time around the matched methods.
     * @param joinPoint The join point for the advised method.
     * @return The result of the original method execution.
     * @throws Throwable if the advised method throws an exception.
     */
    @Around("applicationPackagePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        if (log.isInfoEnabled()) {
            log.info("Enter: {}.{}() with argument[s] = {}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    Arrays.toString(joinPoint.getArgs()));
        }
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        if (log.isInfoEnabled()) {
            log.info("Exit: {}.{}() with result = {}. Execution time: {} ms",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    result,
                    (endTime - startTime));
        }
        return result;
    }
}
```
```java
// src/main/java/com/example/model/BaseEntity.java