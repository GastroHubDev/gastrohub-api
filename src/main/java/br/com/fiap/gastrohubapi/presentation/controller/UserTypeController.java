package br.com.fiap.gastrohubapi.presentation.controller;

import br.com.fiap.gastrohubapi.application.usecase.usertype.CreateUserTypeUseCase;
import br.com.fiap.gastrohubapi.application.usecase.usertype.DeleteUserTypeUseCase;
import br.com.fiap.gastrohubapi.application.usecase.usertype.FindUserTypeByIdUseCase;
import br.com.fiap.gastrohubapi.application.usecase.usertype.ListUserTypesUseCase;
import br.com.fiap.gastrohubapi.application.usecase.usertype.UpdateUserTypeUseCase;
import br.com.fiap.gastrohubapi.domain.entity.UserType;
import br.com.fiap.gastrohubapi.presentation.dto.request.CreateUserTypeRequest;
import br.com.fiap.gastrohubapi.presentation.dto.request.UpdateUserTypeRequest;
import br.com.fiap.gastrohubapi.presentation.dto.response.UserTypeResponse;
import br.com.fiap.gastrohubapi.presentation.mapper.UserTypeMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping
    public ResponseEntity<UserTypeResponse> create(@Valid @RequestBody CreateUserTypeRequest request) {
        UserType userType = createUserTypeUseCase.execute(request.name(), request.baseCategory());
        return ResponseEntity.status(HttpStatus.CREATED).body(UserTypeMapper.toResponse(userType));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserTypeResponse> findById(@PathVariable Long id) {
        UserType userType = findUserTypeByIdUseCase.execute(id);
        return ResponseEntity.ok(UserTypeMapper.toResponse(userType));
    }

    @GetMapping
    public ResponseEntity<List<UserTypeResponse>> findAll() {
        List<UserTypeResponse> response = listUserTypesUseCase.execute()
                .stream()
                .map(UserTypeMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserTypeResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserTypeRequest request
    ) {
        UserType userType = updateUserTypeUseCase.execute(id, request.name(), request.baseCategory());
        return ResponseEntity.ok(UserTypeMapper.toResponse(userType));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteUserTypeUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}