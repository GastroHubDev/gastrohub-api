package br.com.fiap.gastrohubapi.presentation.controller;

import br.com.fiap.gastrohubapi.application.usecase.user.*;
import br.com.fiap.gastrohubapi.application.usecase.user.input.NewUserDTO;
import br.com.fiap.gastrohubapi.application.usecase.user.input.UpdateUserDTO;
import br.com.fiap.gastrohubapi.presentation.dto.request.CreateUserRequest;
import br.com.fiap.gastrohubapi.presentation.dto.request.UpdateUserRequest;
import br.com.fiap.gastrohubapi.presentation.dto.response.UserResponse;
import br.com.fiap.gastrohubapi.presentation.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "User management endpoints")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final FindUserByIdUseCase findUserByIdUseCase;
    private final FindUserByNameUseCase findUserByNameUseCase;
    private final FindUserByEmailUseCase findUserByEmailUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final FindAllUsersUseCase findAllUsersUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final UserMapper userMapper;

    public UserController(CreateUserUseCase createUserUseCase,
                          FindUserByIdUseCase findUserByIdUseCase,
                          FindUserByNameUseCase findUserByNameUseCase,
                          FindUserByEmailUseCase findUserByEmailUseCase,
                          UpdateUserUseCase updateUserUseCase,
                          FindAllUsersUseCase findAllUsersUseCase,
                          DeleteUserUseCase deleteUserUseCase,
                          UserMapper userMapper) {
        this.createUserUseCase = createUserUseCase;
        this.findUserByIdUseCase = findUserByIdUseCase;
        this.findUserByNameUseCase = findUserByNameUseCase;
        this.findUserByEmailUseCase = findUserByEmailUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.findAllUsersUseCase = findAllUsersUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
        this.userMapper = userMapper;
    }

    @Operation(summary = "Create a user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "409", description = "User email already exists")
    })
    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest request) {
        NewUserDTO useCaseInput = new NewUserDTO(null, request.name(), request.email(), request.userType(), request.password());
        var user = this.createUserUseCase.run(useCaseInput);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userMapper.toResponseDTO(user));
    }

    @Operation(summary = "Find a user by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable UUID id) {
        var user = this.findUserByIdUseCase.run(id);
        return ResponseEntity.ok(this.userMapper.toResponseDTO(user));
    }

    @Operation(summary = "Find users by name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users found")
    })
    @GetMapping("/search")
    public ResponseEntity<List<UserResponse>> findByName(@RequestParam String name) {
        var users = this.findUserByNameUseCase.run(name);
        return ResponseEntity.ok(this.userMapper.toResponseDTOList(users));
    }

    @Operation(summary = "Find a user by email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> findByEmail(@PathVariable String email) {
        var user = this.findUserByEmailUseCase.execute(email);
        return ResponseEntity.ok(this.userMapper.toResponseDTO(user));
    }

    @Operation(summary = "Update a user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "409", description = "Email already in use")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable UUID id, @Valid @RequestBody UpdateUserRequest request) {
        UpdateUserDTO useCaseInput = new UpdateUserDTO(id, request.name(), request.email(), request.userType(), request.password());
        var updatedUser = this.updateUserUseCase.execute(useCaseInput);
        return ResponseEntity.ok(this.userMapper.toResponseDTO(updatedUser));
    }

    @Operation(summary = "Find all users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users found")
    })
    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        var users = this.findAllUsersUseCase.execute();
        return ResponseEntity.ok(this.userMapper.toResponseDTOList(users));
    }

    @Operation(summary = "Delete a user")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User deleted"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        this.deleteUserUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}