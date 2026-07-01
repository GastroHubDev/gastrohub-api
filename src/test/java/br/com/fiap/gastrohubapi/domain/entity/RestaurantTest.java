package br.com.fiap.gastrohubapi.domain.entity;

import br.com.fiap.gastrohubapi.domain.enums.KitchenType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RestaurantTest {

    private static final String NAME = "Cantina da Nona";
    private static final String ADDRESS = "Rua das Flores, 123";
    private static final KitchenType KITCHEN_TYPE = KitchenType.ITALIAN;
    private static final String OPENING_HOURS = "08:00-22:00";

    @Nested
    class Create {

        @Test
        void shouldCreateWithValidData() {
            UUID ownerId = UUID.randomUUID();

            Restaurant restaurant = Restaurant.create(NAME, ADDRESS, KITCHEN_TYPE, OPENING_HOURS, ownerId);

            assertThat(restaurant.getId()).isNull();
            assertThat(restaurant.getName()).isEqualTo(NAME);
            assertThat(restaurant.getAddress()).isEqualTo(ADDRESS);
            assertThat(restaurant.getKitchenType()).isEqualTo(KITCHEN_TYPE);
            assertThat(restaurant.getOpeningHours()).isEqualTo(OPENING_HOURS);
            assertThat(restaurant.getRestaurantOwnerId()).isEqualTo(ownerId);
        }

        @Test
        void shouldTrimStringFields() {
            Restaurant restaurant = Restaurant.create(
                    "  Cantina da Nona  ",
                    "  Rua das Flores, 123  ",
                    KITCHEN_TYPE,
                    "  08:00-22:00  ",
                    UUID.randomUUID());

            assertThat(restaurant.getName()).isEqualTo("Cantina da Nona");
            assertThat(restaurant.getAddress()).isEqualTo("Rua das Flores, 123");
            assertThat(restaurant.getOpeningHours()).isEqualTo("08:00-22:00");
        }

        @Test
        void shouldRejectBlankName() {
            assertThatThrownBy(() ->
                    Restaurant.create("   ", ADDRESS, KITCHEN_TYPE, OPENING_HOURS, UUID.randomUUID()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Name cannot be null or empty");
        }

        @Test
        void shouldRejectBlankAddress() {
            assertThatThrownBy(() ->
                    Restaurant.create(NAME, "   ", KITCHEN_TYPE, OPENING_HOURS, UUID.randomUUID()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Address cannot be null or empty");
        }

        @Test
        void shouldRejectBlankOpeningHours() {
            assertThatThrownBy(() ->
                    Restaurant.create(NAME, ADDRESS, KITCHEN_TYPE, "   ", UUID.randomUUID()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("OpeningHours cannot be null or empty");
        }

        @Test
        void shouldRejectNullKitchenType() {
            assertThatThrownBy(() ->
                    Restaurant.create(NAME, ADDRESS, null, OPENING_HOURS, UUID.randomUUID()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("KitchenType cannot be null");
        }

        @Test
        void shouldRejectNullRestaurantOwnerId() {
            assertThatThrownBy(() ->
                    Restaurant.create(NAME, ADDRESS, KITCHEN_TYPE, OPENING_HOURS, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("RestaurantOwnerId cannot be null");
        }

        // Documentacao do comportamento atual: create() chama name.trim() ANTES de validar,
        // entao um nome null estoura NullPointerException, e nao IllegalArgumentException.
        // O mesmo vale para address e openingHours. (Possivel correcao futura.)
        @Test
        void shouldThrowNpeWhenNameIsNullDueToTrimBeforeValidation() {
            assertThatThrownBy(() ->
                    Restaurant.create(null, ADDRESS, KITCHEN_TYPE, OPENING_HOURS, UUID.randomUUID()))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    class Restore {

        @Test
        void shouldRestoreWithValidData() {
            UUID id = UUID.randomUUID();
            UUID ownerId = UUID.randomUUID();

            Restaurant restaurant = Restaurant.restore(id, NAME, ADDRESS, KITCHEN_TYPE, OPENING_HOURS, ownerId);

            assertThat(restaurant.getId()).isEqualTo(id);
            assertThat(restaurant.getName()).isEqualTo(NAME);
            assertThat(restaurant.getAddress()).isEqualTo(ADDRESS);
            assertThat(restaurant.getKitchenType()).isEqualTo(KITCHEN_TYPE);
            assertThat(restaurant.getOpeningHours()).isEqualTo(OPENING_HOURS);
            assertThat(restaurant.getRestaurantOwnerId()).isEqualTo(ownerId);
        }

        @Test
        void shouldRejectNullId() {
            assertThatThrownBy(() ->
                    Restaurant.restore(null, NAME, ADDRESS, KITCHEN_TYPE, OPENING_HOURS, UUID.randomUUID()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Id cannot be null");
        }

        @Test
        void shouldValidateFieldsOnRestore() {
            assertThatThrownBy(() ->
                    Restaurant.restore(UUID.randomUUID(), "   ", ADDRESS, KITCHEN_TYPE, OPENING_HOURS, UUID.randomUUID()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Name cannot be null or empty");
        }
    }

    @Nested
    class Update {

        @Test
        void shouldUpdateAllFields() {
            Restaurant restaurant = Restaurant.create(NAME, ADDRESS, KITCHEN_TYPE, OPENING_HOURS, UUID.randomUUID());
            UUID newOwnerId = UUID.randomUUID();

            restaurant.update("Sushi House", "Av. Central, 99", KitchenType.JAPANESE, "11:00-23:00", newOwnerId);

            assertThat(restaurant.getName()).isEqualTo("Sushi House");
            assertThat(restaurant.getAddress()).isEqualTo("Av. Central, 99");
            assertThat(restaurant.getKitchenType()).isEqualTo(KitchenType.JAPANESE);
            assertThat(restaurant.getOpeningHours()).isEqualTo("11:00-23:00");
            assertThat(restaurant.getRestaurantOwnerId()).isEqualTo(newOwnerId);
        }

        @Test
        void shouldKeepOriginalIdAfterUpdate() {
            UUID id = UUID.randomUUID();
            Restaurant restaurant = Restaurant.restore(id, NAME, ADDRESS, KITCHEN_TYPE, OPENING_HOURS, UUID.randomUUID());

            restaurant.update("Sushi House", "Av. Central, 99", KitchenType.JAPANESE, "11:00-23:00", UUID.randomUUID());

            assertThat(restaurant.getId()).isEqualTo(id);
        }

        @Test
        void shouldRejectInvalidFieldOnUpdate() {
            Restaurant restaurant = Restaurant.create(NAME, ADDRESS, KITCHEN_TYPE, OPENING_HOURS, UUID.randomUUID());

            assertThatThrownBy(() ->
                    restaurant.update("   ", ADDRESS, KITCHEN_TYPE, OPENING_HOURS, UUID.randomUUID()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Name cannot be null or empty");
        }
    }
}
