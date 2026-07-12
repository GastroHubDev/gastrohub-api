package br.com.fiap.gastrohubapi.presentation.controller;

import br.com.fiap.gastrohubapi.application.usecase.menuitem.*;
import br.com.fiap.gastrohubapi.domain.entity.MenuItem;
import br.com.fiap.gastrohubapi.domain.exception.MenuItemNotFoundException;
import br.com.fiap.gastrohubapi.presentation.exceptionhandler.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MenuItemController.class)
@Import(GlobalExceptionHandler.class)
class MenuItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean private CreateMenuItemUseCase createMenuItemUseCase;
    @MockitoBean private FindMenuItemByIdUseCase findMenuItemByIdUseCase;
    @MockitoBean private FindAllMenuItemsUseCase findAllMenuItemsUseCase;
    @MockitoBean private FindMenuItemsByRestaurantUseCase findMenuItemsByRestaurantUseCase;
    @MockitoBean private UpdateMenuItemUseCase updateMenuItemUseCase;
    @MockitoBean private DeleteMenuItemUseCase deleteMenuItemUseCase;

    private static final UUID ITEM_ID = UUID.randomUUID();
    private static final UUID RESTAURANT_ID = UUID.randomUUID();

    private MenuItem sampleItem() {
        return MenuItem.restore(ITEM_ID, "Pizza", "Margherita", new BigDecimal("39.90"),
                true, "/photos/pizza.jpg", RESTAURANT_ID);
    }

    private String validRequestJson() {
        return """
                {
                  "name": "Pizza",
                  "description": "Margherita",
                  "price": 39.90,
                  "onlyInRestaurant": true,
                  "photoPath": "/photos/pizza.jpg",
                  "restaurantId": "%s"
                }
                """.formatted(RESTAURANT_ID);
    }

    @Test
    void createShouldReturn201WithBody() throws Exception {
        when(createMenuItemUseCase.execute(any())).thenReturn(sampleItem());

        mockMvc.perform(post("/menu-items")
                        .contentType("application/json")
                        .content(validRequestJson()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(ITEM_ID.toString()))
                .andExpect(jsonPath("$.name").value("Pizza"))
                .andExpect(jsonPath("$.price").value(39.90));
    }

    @Test
    void createShouldReturn400WhenNameIsBlank() throws Exception {
        String invalidJson = """
                {
                  "name": "",
                  "description": "Margherita",
                  "price": 39.90,
                  "onlyInRestaurant": true,
                  "restaurantId": "%s"
                }
                """.formatted(RESTAURANT_ID);

        mockMvc.perform(post("/menu-items")
                        .contentType("application/json")
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createShouldReturn400WhenPriceBelowMinimum() throws Exception {
        String invalidJson = """
                {
                  "name": "Pizza",
                  "description": "Margherita",
                  "price": 0,
                  "onlyInRestaurant": true,
                  "restaurantId": "%s"
                }
                """.formatted(RESTAURANT_ID);

        mockMvc.perform(post("/menu-items")
                        .contentType("application/json")
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findByIdShouldReturn200() throws Exception {
        when(findMenuItemByIdUseCase.execute(ITEM_ID)).thenReturn(sampleItem());

        mockMvc.perform(get("/menu-items/{id}", ITEM_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ITEM_ID.toString()));
    }

    @Test
    void findByIdShouldReturn404WhenNotFound() throws Exception {
        when(findMenuItemByIdUseCase.execute(ITEM_ID))
                .thenThrow(new MenuItemNotFoundException(ITEM_ID));

        mockMvc.perform(get("/menu-items/{id}", ITEM_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void findAllShouldReturn200WithList() throws Exception {
        when(findAllMenuItemsUseCase.execute()).thenReturn(List.of(sampleItem()));

        mockMvc.perform(get("/menu-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(ITEM_ID.toString()));
    }

    @Test
    void findByRestaurantShouldReturn200WithList() throws Exception {
        when(findMenuItemsByRestaurantUseCase.execute(RESTAURANT_ID))
                .thenReturn(List.of(sampleItem()));

        mockMvc.perform(get("/menu-items/restaurant/{restaurantId}", RESTAURANT_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].restaurantId").value(RESTAURANT_ID.toString()));
    }

    @Test
    void updateShouldReturn200() throws Exception {
        when(updateMenuItemUseCase.execute(eq(ITEM_ID), any())).thenReturn(sampleItem());

        mockMvc.perform(put("/menu-items/{id}", ITEM_ID)
                        .contentType("application/json")
                        .content(validRequestJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ITEM_ID.toString()));
    }

    @Test
    void deleteShouldReturn204() throws Exception {
        mockMvc.perform(delete("/menu-items/{id}", ITEM_ID))
                .andExpect(status().isNoContent());

        verify(deleteMenuItemUseCase).execute(ITEM_ID);
    }
}
