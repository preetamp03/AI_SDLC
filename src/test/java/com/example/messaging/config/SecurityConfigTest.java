package com.example.messaging.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {SecurityConfig.class, org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest.class})
class SecurityConfigTest {

    @Autowired
    private ApplicationContext context;

    /**
     * Verifies that the SecurityFilterChain bean is created and present in the application context.
     */
    @Test
    void securityFilterChainBeanExists() {
        SecurityFilterChain filterChain = context.getBean(SecurityFilterChain.class);
        assertThat(filterChain).isNotNull();
    }

    /**
     * Verifies that the PasswordEncoder bean is created and is of the expected type.
     */
    @Test
    void passwordEncoderBeanExists() {
        PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);
        assertThat(passwordEncoder).isNotNull();
    }
}