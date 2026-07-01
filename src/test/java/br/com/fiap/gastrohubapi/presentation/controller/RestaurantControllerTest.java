package br.com.fiap.gastrohubapi.presentation.controller;

import br.com.fiap.gastrohubapi.application.usecase.restaurant.CreateRestaurantUseCase;
import br.com.fiap.gastrohubapi.application.usecase.restaurant.DeleteRestaurantUseCase;
import br.com.fiap.gastrohubapi.application.usecase.restaurant.FindRestaurantByIdUseCase;
import br.com.fiap.gastrohubapi.application.usecase.restaurant.FindRestaurantByNameUseCase;
import br.com.fiap.gastrohubapi.application.usecase.restaurant.ListRestaurantsUseCase;
import br.com.fiap.gastrohubapi.application.usecase.restaurant.UpdateRestaurantUseCase;
import br.com.fiap.gastrohubapi.application.usecase.restaurant.input.NewRestaurantInput;
import br.com.fiap.gastrohubapi.application.usecase.restaurant.input.UpdateRestaurantInput;
import br.com.fiap.gastrohubapi.domain.entity.Restaurant;
import br.com.fiap.gastrohubapi.domain.enums.KitchenType;
import br.com.fiap.gastrohubapi.domain.exception.RestaurantAlreadyExistsException;
import br.com.fiap.gastrohubapi.domain.exception.RestaurantNotFoundByIdException;
import br.com.fiap.gastrohubapi.domain.exception.UserNotFoundException;
import br.com.fiap.gastrohubapi.domain.exception.UserTypeNotAllowedForRestaurantOwnerException;
import br.com.fiap.gastrohubapi.presentation.dto.request.CreateRestaurantRequest;
import br.com.fiap.gastrohubapi.presentation.dto.request.UpdateRestaurantRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RestaurantController.class)
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateRestaurantUseCase createRestaurantUseCase;
    @MockitoBean
    private UpdateRestaurantUseCase updateRestaurantUseCase;
    @MockitoBean
    private FindRestaurantByIdUseCase findRestaurantByIdUseCase;
    @MockitoBean
    private FindRestaurantByNameUseCase findRestaurantByNameUseCase;
    @MockitoBean
    private ListRestaurantsUseCase listRestaurantsUseCase;
    @MockitoBean
    private DeleteRestaurantUseCase deleteRestaurantUseCase;

    private static final String NAME = "Cantina da Nona";
    private static final String ADDRESS = "Rua das Flores, 123";
    private static final KitchenType KITCHEN_TYPE = KitchenType.ITALIAN;
    private static final String OPENING_HOURS = "08:00-22:00";

    private Restaurant restaurant(UUID id, UUID ownerId) {
        return Restaurant.restore(id, NAME, ADDRESS, KITCHEN_TYPE, OPENING_HOURS, ownerId);
    }

    private CreateRestaurantRequest validCreateRequest(UUID ownerId) {
        return new CreateRestaurantRequest(NAME, ADDRESS, KITCHEN_TYPE, OPENING_HOURS, ownerId);
    }

    private UpdateRestaurantRequest validUpdateRequest(UUID ownerId) {
        return new UpdateRestaurantRequest(NAME, ADDRESS, KITCHEN_TYPE, OPENING_HOURS, ownerId);
    }

    // ---------- POST /restaurants ----------

    @Test
    void shouldCreateRestaurantAndReturn201() throws Exception {
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        when(createRestaurantUseCase.run(any(NewRestaurantInput.class))).thenReturn(restaurant(id, ownerId));

        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validCreateRequest(ownerId))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.kitchenType").value("ITALIAN"))
                .andExpect(jsonPath("$.restaurantOwnerId").value(ownerId.toString()));

        verify(createRestaurantUseCase).run(any(NewRestaurantInput.class));
    }

    @Test
    void shouldReturn400WhenCreateRequestIsInvalid() throws Exception {
        CreateRestaurantRequest invalid = new CreateRestaurantRequest(
                "  ", ADDRESS, KITCHEN_TYPE, OPENING_HOURS, UUID.randomUUID());

        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());

        verify(createRestaurantUseCase, never()).run(any());
    }

    @Test
    void shouldReturn404WhenOwnerNotFoundOnCreate() throws Exception {
        UUID ownerId = UUID.randomUUID();
        when(createRestaurantUseCase.run(any(NewRestaurantInput.class)))
                .thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validCreateRequest(ownerId))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    void shouldReturn400WhenOwnerIsClientOnCreate() throws Exception {
        UUID ownerId = UUID.randomUUID();
        when(createRestaurantUseCase.run(any(NewRestaurantInput.class)))
                .thenThrow(new UserTypeNotAllowedForRestaurantOwnerException("Owner cannot be a CLIENT"));

        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validCreateRequest(ownerId))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn409WhenRestaurantAlreadyExistsOnCreate() throws Exception {
        UUID ownerId = UUID.randomUUID();
        when(createRestaurantUseCase.run(any(NewRestaurantInput.class)))
                .thenThrow(new RestaurantAlreadyExistsException("Already exists"));

        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validCreateRequest(ownerId))))
                .andExpect(status().isConflict());
    }

    // ---------- GET /restaurants/{id} ----------

    @Test
    void shouldReturnRestaurantByIdWith200() throws Exception {
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        when(findRestaurantByIdUseCase.run(id)).thenReturn(restaurant(id, ownerId));

        mockMvc.perform(get("/restaurants/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value(NAME));
    }

    @Test
    void shouldReturn404WhenRestaurantByIdNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(findRestaurantByIdUseCase.run(id))
                .thenThrow(new RestaurantNotFoundByIdException("Restaurant id: " + id));

        mockMvc.perform(get("/restaurants/{id}", id))
                .andExpect(status().isNotFound());
    }

    // ---------- GET /restaurants/search ----------

    @Test
    void shouldSearchRestaurantsByNameWith200() throws Exception {
        UUID id = UUID.randomUUID();
        when(findRestaurantByNameUseCase.run(NAME)).thenReturn(List.of(restaurant(id, UUID.randomUUID())));

        mockMvc.perform(get("/restaurants/search").param("name", NAME))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(id.toString()));
    }

    // ---------- GET /restaurants ----------

    @Test
    void shouldListAllRestaurantsWith200() throws Exception {
        when(listRestaurantsUseCase.run()).thenReturn(List.of(
                restaurant(UUID.randomUUID(), UUID.randomUUID()),
                restaurant(UUID.randomUUID(), UUID.randomUUID())));

        mockMvc.perform(get("/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // ---------- PUT /restaurants/{id} ----------

    @Test
    void shouldUpdateRestaurantAndReturn200() throws Exception {
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        when(updateRestaurantUseCase.run(eq(id), any(UpdateRestaurantInput.class)))
                .thenReturn(restaurant(id, ownerId));

        mockMvc.perform(put("/restaurants/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUpdateRequest(ownerId))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));

        verify(updateRestaurantUseCase).run(eq(id), any(UpdateRestaurantInput.class));
    }

    @Test
    void shouldReturn400WhenUpdateRequestIsInvalid() throws Exception {
        UUID id = UUID.randomUUID();
        UpdateRestaurantRequest invalid = new UpdateRestaurantRequest(
                NAME, ADDRESS, null, OPENING_HOURS, UUID.randomUUID());

        mockMvc.perform(put("/restaurants/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());

        verify(updateRestaurantUseCase, never()).run(any(), any());
    }

    @Test
    void shouldReturn404WhenRestaurantToUpdateNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        when(updateRestaurantUseCase.run(eq(id), any(UpdateRestaurantInput.class)))
                .thenThrow(new RestaurantNotFoundByIdException("Restaurant not found"));

        mockMvc.perform(put("/restaurants/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUpdateRequest(ownerId))))
                .andExpect(status().isNotFound());
    }

    // ---------- DELETE /restaurants/{id} ----------

    @Test
    void shouldDeleteRestaurantAndReturn204() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/restaurants/{id}", id))
                .andExpect(status().isNoContent());

        verify(deleteRestaurantUseCase).run(id);
    }

    @Test
    void shouldReturn404WhenRestaurantToDeleteNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        org.mockito.Mockito.doThrow(new RestaurantNotFoundByIdException("Restaurant id: " + id))
                .when(deleteRestaurantUseCase).run(id);

        mockMvc.perform(delete("/restaurants/{id}", id))
                .andExpect(status().isNotFound());
    }
}
