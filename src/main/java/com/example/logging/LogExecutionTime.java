package com.example.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A custom annotation to mark methods for execution time logging.
 * When a method is annotated with @LogExecutionTime, an AOP aspect
 * will log the time it took to execute.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogExecutionTime {
}
```
```java
// src/main/java/com/example/logging/LoggingAspect.java