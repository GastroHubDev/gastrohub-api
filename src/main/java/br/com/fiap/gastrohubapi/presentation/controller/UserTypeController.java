package br.com.fiap.gastrohubapi.presentation.controller;

import br.com.fiap.gastrohubapi.application.usecase.usertype.*;
import br.com.fiap.gastrohubapi.domain.entity.UserType;
import br.com.fiap.gastrohubapi.presentation.dto.request.CreateUserTypeRequest;
import br.com.fiap.gastrohubapi.presentation.dto.request.UpdateUserTypeRequest;
import br.com.fiap.gastrohubapi.presentation.dto.response.UserTypeResponse;
import br.com.fiap.gastrohubapi.presentation.mapper.UserTypeMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User Types", description = "User type management endpoints")
@RestController
@RequestMapping("/user-types")
public class UserTypeController {

    private final CreateUserTypeUseCase createUserTypeUseCase;
    private final FindUserTypeByIdUseCase findUserTypeByIdUseCase;
    private final ListUserTypesUseCase listUserTypesUseCase;
    private final UpdateUserTypeUseCase updateUserTypeUseCase;
    private final DeleteUserTypeUseCase deleteUserTypeUseCase;

    public UserTypeController(
            CreateUserTypeUseCase createUserTypeUseCase,
            FindUserTypeByIdUseCase findUserTypeByIdUseCase,
            ListUserTypesUseCase listUserTypesUseCase,
            UpdateUserTypeUseCase updateUserTypeUseCase,
            DeleteUserTypeUseCase deleteUserTypeUseCase
    ) {
        this.createUserTypeUseCase = createUserTypeUseCase;
        this.findUserTypeByIdUseCase = findUserTypeByIdUseCase;
        this.listUserTypesUseCase = listUserTypesUseCase;
        this.updateUserTypeUseCase = updateUserTypeUseCase;
        this.deleteUserTypeUseCase = deleteUserTypeUseCase;
    }

    @Operation(summary = "Create a user type")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User type created"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "409", description = "User type name already exists")
    })
    @PostMapping
    public ResponseEntity<UserTypeResponse> create(@Valid @RequestBody CreateUserTypeRequest request) {
        UserType userType = createUserTypeUseCase.execute(request.name(), request.baseCategory());
        return ResponseEntity.status(HttpStatus.CREATED).body(UserTypeMapper.toResponse(userType));
    }

    @Operation(summary = "Find a user type by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User type found"),
            @ApiResponse(responseCode = "404", description = "User type not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserTypeResponse> findById(@PathVariable Long id) {
        UserType userType = findUserTypeByIdUseCase.execute(id);
        return ResponseEntity.ok(UserTypeMapper.toResponse(userType));
    }

    @Operation(summary = "List all user types")
    @ApiResponse(responseCode = "200", description = "User types listed")
    @GetMapping
    public ResponseEntity<List<UserTypeResponse>> findAll() {
        List<UserTypeResponse> response = listUserTypesUseCase.execute()
                .stream()
                .map(UserTypeMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update a user type")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User type updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "User type not found"),
            @ApiResponse(responseCode = "409", description = "User type name already exists or user type is in use")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserTypeResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserTypeRequest request
    ) {
        UserType userType = updateUserTypeUseCase.execute(id, request.name(), request.baseCategory());
        return ResponseEntity.ok(UserTypeMapper.toResponse(userType));
    }

    @Operation(summary = "Delete a user type")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User type deleted"),
            @ApiResponse(responseCode = "404", description = "User type not found"),
            @ApiResponse(responseCode = "409", description = "User type is in use")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteUserTypeUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}