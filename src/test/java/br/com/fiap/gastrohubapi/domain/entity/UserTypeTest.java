package br.com.fiap.gastrohubapi.domain.entity;

import java.util.UUID;
import br.com.fiap.gastrohubapi.domain.enums.BaseCategory;
import br.com.fiap.gastrohubapi.domain.exception.InvalidUserTypeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserTypeTest {
    private static final UUID TYPE_ID_1 = UUID.fromString("11111111-1111-1111-1111-111111111111");


    @Test
    void shouldCreateUserType() {
        UserType userType = UserType.restore(TYPE_ID_1, "Owner", BaseCategory.OWNER);

        assertEquals(TYPE_ID_1, userType.getId());
        assertEquals("Owner", userType.getName());
        assertEquals(BaseCategory.OWNER, userType.getBaseCategory());
    }

    @Test
    void shouldTrimNameWhenCreatingUserType() {
        UserType userType = UserType.restore(TYPE_ID_1, "  Client  ", BaseCategory.CLIENT);

        assertEquals("Client", userType.getName());
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        InvalidUserTypeException exception = assertThrows(
                InvalidUserTypeException.class,
                () -> UserType.restore(TYPE_ID_1, null, BaseCategory.CLIENT)
        );

        assertEquals("User type name is required.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        InvalidUserTypeException exception = assertThrows(
                InvalidUserTypeException.class,
                () -> UserType.restore(TYPE_ID_1, "   ", BaseCategory.CLIENT)
        );

        assertEquals("User type name is required.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenBaseCategoryIsNull() {
        InvalidUserTypeException exception = assertThrows(
                InvalidUserTypeException.class,
                () -> UserType.restore(TYPE_ID_1, "Client", null)
        );

        assertEquals("Base category is required.", exception.getMessage());
    }

    @Test
    void shouldUpdateUserType() {
        UserType userType = UserType.restore(TYPE_ID_1, "Client", BaseCategory.CLIENT);

        userType.update("Owner", BaseCategory.OWNER);

        assertEquals("Owner", userType.getName());
        assertEquals(BaseCategory.OWNER, userType.getBaseCategory());
    }

    @Test
    void shouldTrimNameWhenUpdatingUserType() {
        UserType userType = UserType.restore(TYPE_ID_1, "Client", BaseCategory.CLIENT);

        userType.update("  Owner  ", BaseCategory.OWNER);

        assertEquals("Owner", userType.getName());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithBlankName() {
        UserType userType = UserType.restore(TYPE_ID_1, "Client", BaseCategory.CLIENT);

        InvalidUserTypeException exception = assertThrows(
                InvalidUserTypeException.class,
                () -> userType.update("   ", BaseCategory.OWNER)
        );

        assertEquals("User type name is required.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithNullBaseCategory() {
        UserType userType = UserType.restore(TYPE_ID_1, "Client", BaseCategory.CLIENT);

        InvalidUserTypeException exception = assertThrows(
                InvalidUserTypeException.class,
                () -> userType.update("Owner", null)
        );

        assertEquals("Base category is required.", exception.getMessage());
    }
}