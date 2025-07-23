package com.example.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Tests that a whitelisted endpoint is publicly accessible.
     */
    @Test
    void whenAccessingPublicUrl_thenIsOk() throws Exception {
        mockMvc.perform(get("/swagger-ui.html"))
               .andExpect(status().isOk());
    }

    /**
     * Tests that a secured endpoint returns 401 Unauthorized without authentication.
     */
    @Test
    void whenAccessingSecuredUrl_thenIsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/chats"))
               .andExpect(status().isUnauthorized());
    }
}
```
```java
// src/test/java/com/example/config/ApplicationConfigTest.java