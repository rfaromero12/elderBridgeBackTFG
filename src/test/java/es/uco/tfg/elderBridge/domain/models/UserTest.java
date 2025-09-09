package es.uco.tfg.elderBridge.domain.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("User Domain Model Tests")
class UserTest {

    private User validUser;

    @BeforeEach
    void setUp() {
        validUser = User.builder()
                .userId(1L)
                .name("Juan")
                .surname("Pérez")
                .email("juan.perez@example.com")
                .password("hashedPassword123")
                .rol("USER")
                .build();
    }

    @Test
    @DisplayName("1. Should create user with builder pattern")
    void shouldCreateUserWithBuilderPattern() {
        // Given & When (ya creado en setUp)
        
        // Then
        assertNotNull(validUser);
        assertEquals(1L, validUser.getUserId());
        assertEquals("Juan", validUser.getName());
        assertEquals("Pérez", validUser.getSurname());
        assertEquals("juan.perez@example.com", validUser.getEmail());
        assertEquals("hashedPassword123", validUser.getPassword());
        assertEquals("USER", validUser.getRol());
    }

    @Test
    @DisplayName("2. Should create user with no-args constructor")
    void shouldCreateUserWithNoArgsConstructor() {
        // When
        User user = new User();
        
        // Then
        assertNotNull(user);
        assertNull(user.getUserId());
        assertNull(user.getName());
        assertNull(user.getSurname());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertNull(user.getRol());
    }

    @Test
    @DisplayName("3. Should update user fields correctly")
    void shouldUpdateUserFieldsCorrectly() {
        // Given
        User user = new User();
        
        // When
        user.setUserId(5L);
        user.setName("Carlos");
        user.setSurname("Rodríguez");
        user.setEmail("carlos.rodriguez@example.com");
        user.setPassword("newPassword123");
        user.setRol("ADMIN");
        
        // Then
        assertEquals(5L, user.getUserId());
        assertEquals("Carlos", user.getName());
        assertEquals("Rodríguez", user.getSurname());
        assertEquals("carlos.rodriguez@example.com", user.getEmail());
        assertEquals("newPassword123", user.getPassword());
        assertEquals("ADMIN", user.getRol());
    }

    @Test
    @DisplayName("4. Should handle null values")
    void shouldHandleNullValues() {
        // When
        User userWithNulls = User.builder()
                .userId(null)
                .name(null)
                .surname(null)
                .email(null)
                .password(null)
                .rol(null)
                .build();
        
        // Then
        assertNotNull(userWithNulls);
        assertNull(userWithNulls.getUserId());
        assertNull(userWithNulls.getName());
        assertNull(userWithNulls.getSurname());
        assertNull(userWithNulls.getEmail());
        assertNull(userWithNulls.getPassword());
        assertNull(userWithNulls.getRol());
    }
} 