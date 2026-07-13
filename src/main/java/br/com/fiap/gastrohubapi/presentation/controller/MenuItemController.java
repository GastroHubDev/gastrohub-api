package br.com.fiap.gastrohubapi.presentation.controller;

import br.com.fiap.gastrohubapi.application.usecase.menuitem.*;
import br.com.fiap.gastrohubapi.domain.entity.MenuItem;
import br.com.fiap.gastrohubapi.presentation.dto.request.CreateMenuItemRequest;
import br.com.fiap.gastrohubapi.presentation.dto.request.UpdateMenuItemRequest;
import br.com.fiap.gastrohubapi.presentation.dto.response.MenuItemResponse;
import br.com.fiap.gastrohubapi.presentation.mapper.MenuItemMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/menu-items")
@Tag(name = "Menu Items", description = "CRUD for restaurant menu items")
public class MenuItemController {

    private final CreateMenuItemUseCase createMenuItemUseCase;
    private final FindMenuItemByIdUseCase findMenuItemByIdUseCase;
    private final FindAllMenuItemsUseCase findAllMenuItemsUseCase;
    private final FindMenuItemsByRestaurantUseCase findMenuItemsByRestaurantUseCase;
    private final UpdateMenuItemUseCase updateMenuItemUseCase;
    private final DeleteMenuItemUseCase deleteMenuItemUseCase;

    public MenuItemController(CreateMenuItemUseCase createMenuItemUseCase,
                              FindMenuItemByIdUseCase findMenuItemByIdUseCase,
                              FindAllMenuItemsUseCase findAllMenuItemsUseCase,
                              FindMenuItemsByRestaurantUseCase findMenuItemsByRestaurantUseCase,
                              UpdateMenuItemUseCase updateMenuItemUseCase,
                              DeleteMenuItemUseCase deleteMenuItemUseCase) {
        this.createMenuItemUseCase = createMenuItemUseCase;
        this.findMenuItemByIdUseCase = findMenuItemByIdUseCase;
        this.findAllMenuItemsUseCase = findAllMenuItemsUseCase;
        this.findMenuItemsByRestaurantUseCase = findMenuItemsByRestaurantUseCase;
        this.updateMenuItemUseCase = updateMenuItemUseCase;
        this.deleteMenuItemUseCase = deleteMenuItemUseCase;
    }

    @PostMapping
    @Operation(summary = "Create a menu item")
    public ResponseEntity<MenuItemResponse> create(@Valid @RequestBody CreateMenuItemRequest request) {
        MenuItem created = createMenuItemUseCase.execute(MenuItemMapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(MenuItemMapper.toResponse(created));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find a menu item by id")
    public ResponseEntity<MenuItemResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(MenuItemMapper.toResponse(findMenuItemByIdUseCase.execute(id)));
    }

    @GetMapping
    @Operation(summary = "List all menu items")
    public ResponseEntity<List<MenuItemResponse>> findAll() {
        List<MenuItemResponse> items = findAllMenuItemsUseCase.execute().stream()
                .map(MenuItemMapper::toResponse).toList();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/restaurant/{restaurantId}")
    @Operation(summary = "List menu items by restaurant")
    public ResponseEntity<List<MenuItemResponse>> findByRestaurant(@PathVariable UUID restaurantId) {
        List<MenuItemResponse> items = findMenuItemsByRestaurantUseCase.execute(restaurantId).stream()
                .map(MenuItemMapper::toResponse).toList();
        return ResponseEntity.ok(items);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a menu item")
    public ResponseEntity<MenuItemResponse> update(@PathVariable UUID id,
                                                   @Valid @RequestBody UpdateMenuItemRequest request) {
        MenuItem updated = updateMenuItemUseCase.execute(id, MenuItemMapper.toDomain(request));
        return ResponseEntity.ok(MenuItemMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a menu item")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteMenuItemUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}