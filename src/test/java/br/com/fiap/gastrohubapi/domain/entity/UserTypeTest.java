package br.com.fiap.gastrohubapi.domain.entity;

import br.com.fiap.gastrohubapi.domain.exception.InvalidUserTypeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserTypeTest {

    @Test
    void shouldCreateUserType() {
        UserType userType = new UserType(1L, "Owner", BaseCategory.OWNER);

        assertEquals(1L, userType.getId());
        assertEquals("Owner", userType.getName());
        assertEquals(BaseCategory.OWNER, userType.getBaseCategory());
    }

    @Test
    void shouldTrimNameWhenCreatingUserType() {
        UserType userType = new UserType(1L, "  Client  ", BaseCategory.CLIENT);

        assertEquals("Client", userType.getName());
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        InvalidUserTypeException exception = assertThrows(
                InvalidUserTypeException.class,
                () -> new UserType(1L, null, BaseCategory.CLIENT)
        );

        assertEquals("User type name is required.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        InvalidUserTypeException exception = assertThrows(
                InvalidUserTypeException.class,
                () -> new UserType(1L, "   ", BaseCategory.CLIENT)
        );

        assertEquals("User type name is required.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenBaseCategoryIsNull() {
        InvalidUserTypeException exception = assertThrows(
                InvalidUserTypeException.class,
                () -> new UserType(1L, "Client", null)
        );

        assertEquals("Base category is required.", exception.getMessage());
    }

    @Test
    void shouldUpdateUserType() {
        UserType userType = new UserType(1L, "Client", BaseCategory.CLIENT);

        userType.update("Owner", BaseCategory.OWNER);

        assertEquals("Owner", userType.getName());
        assertEquals(BaseCategory.OWNER, userType.getBaseCategory());
    }

    @Test
    void shouldTrimNameWhenUpdatingUserType() {
        UserType userType = new UserType(1L, "Client", BaseCategory.CLIENT);

        userType.update("  Owner  ", BaseCategory.OWNER);

        assertEquals("Owner", userType.getName());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithBlankName() {
        UserType userType = new UserType(1L, "Client", BaseCategory.CLIENT);

        InvalidUserTypeException exception = assertThrows(
                InvalidUserTypeException.class,
                () -> userType.update("   ", BaseCategory.OWNER)
        );

        assertEquals("User type name is required.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithNullBaseCategory() {
        UserType userType = new UserType(1L, "Client", BaseCategory.CLIENT);

        InvalidUserTypeException exception = assertThrows(
                InvalidUserTypeException.class,
                () -> userType.update("Owner", null)
        );

        assertEquals("Base category is required.", exception.getMessage());
    }
}