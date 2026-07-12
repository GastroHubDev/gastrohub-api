package br.com.fiap.gastrohubapi.domain.entity;

import br.com.fiap.gastrohubapi.domain.enums.BaseCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private static final UUID TYPE_ID_1 = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID TYPE_ID_2 = UUID.fromString("22222222-2222-2222-2222-222222222222");


    private static final String VALID_NAME = "Joao";
    private static final String VALID_EMAIL = "Joao@test.com";
    private static final String VALID_PASSWORD = "password123";

    private UserType clientUserType;
    private UserType adminUserType;

    @BeforeEach
    void setUp() {
        clientUserType = UserType.restore(TYPE_ID_1, "CLIENT", BaseCategory.CLIENT);

        adminUserType = UserType.restore(TYPE_ID_2, "ADMIN", BaseCategory.OWNER);
    }


    @Test
    void shouldCreateNewUserSuccessfully() {
        User user = User.create(VALID_NAME, VALID_EMAIL, VALID_PASSWORD, clientUserType);

        assertNotNull(user);
        assertNotNull(user.getId());
        assertEquals(VALID_NAME, user.getName());
        assertEquals(VALID_EMAIL, user.getEmail());
        assertEquals(VALID_PASSWORD, user.getPassword());
        assertEquals(clientUserType, user.getUserType());
    }

    @Test
    void shouldRestoreExistingUserSuccessfully() {
        UUID existingId = UUID.randomUUID();
        User user = User.restore(existingId, VALID_NAME, VALID_EMAIL, VALID_PASSWORD, clientUserType);

        assertNotNull(user);
        assertEquals(existingId, user.getId());
        assertEquals(VALID_NAME, user.getName());
    }


    @Test
    void shouldUpdateUserSuccessfully() {
        User user = User.create("Old Name", "old@test.com", "old123", clientUserType);
        UUID originalId = user.getId();

        user.update(VALID_NAME, VALID_EMAIL, VALID_PASSWORD, adminUserType);

        assertEquals(originalId, user.getId());
        assertEquals(VALID_NAME, user.getName());
        assertEquals(VALID_EMAIL, user.getEmail());
        assertEquals(VALID_PASSWORD, user.getPassword());
        assertEquals(adminUserType, user.getUserType());
    }

    @Test
    void shouldReturnTrueWhenUserIsClient() {
        User user = User.create(VALID_NAME, VALID_EMAIL, VALID_PASSWORD, clientUserType);
        assertTrue(user.isClient());
    }

    @Test
    void shouldReturnFalseWhenUserIsNotClient() {
        User user = User.create(VALID_NAME, VALID_EMAIL, VALID_PASSWORD, adminUserType);
        assertFalse(user.isClient());
    }


    @Test
    void shouldThrowExceptionWhenNameIsInvalid() {
        IllegalArgumentException exceptionNull = assertThrows(IllegalArgumentException.class,
                () -> User.create(null, VALID_EMAIL, VALID_PASSWORD, clientUserType));

        IllegalArgumentException exceptionEmpty = assertThrows(IllegalArgumentException.class,
                () -> User.create("   ", VALID_EMAIL, VALID_PASSWORD, clientUserType));

        assertEquals("User name cannot be null or empty.", exceptionNull.getMessage());
        assertEquals("User name cannot be null or empty.", exceptionEmpty.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenEmailIsInvalid() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> User.create(VALID_NAME, "", VALID_PASSWORD, clientUserType));

        assertEquals("Email cannot be null or empty.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsInvalid() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> User.create(VALID_NAME, VALID_EMAIL, null, clientUserType));

        assertEquals("Password cannot be null or empty.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenUserTypeIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> User.create(VALID_NAME, VALID_EMAIL, VALID_PASSWORD, null));

        assertEquals("User type is required.", exception.getMessage());
    }
}