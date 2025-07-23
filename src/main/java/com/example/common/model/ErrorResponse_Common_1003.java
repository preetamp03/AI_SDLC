package com.example.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * A generic structure for API error responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse_Common_1003 {
    private String errorCode;
    private String message;
    private Map<String, String> invalidFields;
    private Map<String, String> details;
}
```
```java
// src/main/java/com/example/common/model/ErrorCode_Common_1004.java