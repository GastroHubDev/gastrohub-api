package br.com.fiap.gastrohubapi.presentation.controller;

import br.com.fiap.gastrohubapi.domain.entity.BaseCategory;
import br.com.fiap.gastrohubapi.infrastructure.persistence.entity.UserJpaEntity;
import br.com.fiap.gastrohubapi.infrastructure.persistence.entity.UserTypeJpaEntity;
import br.com.fiap.gastrohubapi.infrastructure.persistence.repository.UserRepository;
import br.com.fiap.gastrohubapi.infrastructure.persistence.repository.UserTypeJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class RestaurantControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTypeJpaRepository userTypeJpaRepository;

    private UUID ownerId;

    @BeforeEach
    void setUp() {
        UserTypeJpaEntity ownerType = userTypeJpaRepository.save(
                new UserTypeJpaEntity(null, "Owner-" + UUID.randomUUID(), BaseCategory.OWNER));

        UserJpaEntity owner = userRepository.save(new UserJpaEntity(
                null, "Paula", "paula" + UUID.randomUUID() + "@test.com",
                "senha123", ownerType));

        ownerId = owner.getId();
    }

    private String createRestaurantRequest(String name, String address, UUID restaurantOwnerId) {
        return """
                {
                  "name": "%s",
                  "address": "%s",
                  "kitchenType": "ITALIAN",
                  "openingHours": "10:00-22:00",
                  "restaurantOwnerId": "%s"
                }
                """.formatted(name, address, restaurantOwnerId);
    }

    @Test
    void shouldCreateRestaurantSuccessfully() throws Exception {
        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRestaurantRequest("Restaurante Bom", "Rua 1, 123", ownerId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Restaurante Bom"))
                .andExpect(jsonPath("$.restaurantOwnerId").value(ownerId.toString()));
    }

    @Test
    void shouldReturnConflictWhenCreatingDuplicateRestaurant() throws Exception {
        String request = createRestaurantRequest("Restaurante Bom", "Rua 1, 123", ownerId);

        mockMvc.perform(post("/restaurants").contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/restaurants").contentType(MediaType.APPLICATION_JSON).content(request))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturnBadRequestWhenNameIsBlank() throws Exception {
        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRestaurantRequest("", "Rua 1, 123", ownerId)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnNotFoundWhenOwnerDoesNotExist() throws Exception {
        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRestaurantRequest("Restaurante Bom", "Rua 1, 123", UUID.randomUUID())))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldFindRestaurantById() throws Exception {
        String id = mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRestaurantRequest("Restaurante Bom", "Rua 1, 123", ownerId)))
                .andReturn().getResponse().getContentAsString();

        String restaurantId = id.split("\"id\":\"")[1].split("\"")[0];

        mockMvc.perform(get("/restaurants/{id}", restaurantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Restaurante Bom"));
    }

    @Test
    void shouldReturnNotFoundWhenRestaurantIdDoesNotExist() throws Exception {
        mockMvc.perform(get("/restaurants/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldFindRestaurantsByName() throws Exception {
        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRestaurantRequest("Restaurante Bom", "Rua 1, 123", ownerId)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/restaurants/search").param("name", "Restaurante Bom"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Restaurante Bom"));
    }

    @Test
    void shouldListAllRestaurants() throws Exception {
        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRestaurantRequest("Restaurante Bom", "Rua 1, 123", ownerId)))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRestaurantRequest("Pizzaria D'boa", "Rua 2, 45", ownerId)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void shouldUpdateRestaurantSuccessfully() throws Exception {
        String created = mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRestaurantRequest("Restaurante Bom", "Rua 1, 123", ownerId)))
                .andReturn().getResponse().getContentAsString();
        String restaurantId = created.split("\"id\":\"")[1].split("\"")[0];

        String updateRequest = """
                {
                  "name": "Sushi Bom",
                  "address": "Rua 2, 456",
                  "kitchenType": "ITALIAN",
                  "openingHours": "11:00-23:00",
                  "restaurantOwnerId": "%s"
                }
                """.formatted(ownerId);

        mockMvc.perform(put("/restaurants/{id}", restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Sushi Bom"))
                .andExpect(jsonPath("$.address").value("Rua 2, 456"));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentRestaurant() throws Exception {
        String updateRequest = createRestaurantRequest("Restaurante Bom", "Rua 1, 123", ownerId);

        mockMvc.perform(put("/restaurants/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequest))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteRestaurantSuccessfully() throws Exception {
        String created = mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRestaurantRequest("Restaurante Bom", "Rua 1, 123", ownerId)))
                .andReturn().getResponse().getContentAsString();
        String restaurantId = created.split("\"id\":\"")[1].split("\"")[0];

        mockMvc.perform(delete("/restaurants/{id}", restaurantId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/restaurants/{id}", restaurantId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentRestaurant() throws Exception {
        mockMvc.perform(delete("/restaurants/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
}
