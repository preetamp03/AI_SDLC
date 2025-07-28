package com.example.messaging.security;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class UserPrincipalTest {

    /**
     * Tests the constructor and getters of the UserPrincipal class.
     */
    @Test
    void testUserPrincipal() {
        UUID id = UUID.randomUUID();
        String username = "1234567890";
        String password = "password";
        UserPrincipal principal = new UserPrincipal(id, username, password);

        assertThat(principal.getId()).isEqualTo(id);
        assertThat(principal.getUsername()).isEqualTo(username);
        assertThat(principal.getPassword()).isEqualTo(password);
        assertThat(principal.getAuthorities()).isNotNull().isEmpty();
        assertThat(principal.isAccountNonExpired()).isTrue();
        assertThat(principal.isAccountNonLocked()).isTrue();
        assertThat(principal.isCredentialsNonExpired()).isTrue();
        assertThat(principal.isEnabled()).isTrue();
    }
}