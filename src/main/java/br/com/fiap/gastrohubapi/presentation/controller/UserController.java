package br.com.fiap.gastrohubapi.presentation.controller;

import br.com.fiap.gastrohubapi.application.gateway.UserGateway;
import br.com.fiap.gastrohubapi.application.usecase.user.CreateUserUseCase;
import br.com.fiap.gastrohubapi.application.usecase.user.FindUserByIdUseCase;
import br.com.fiap.gastrohubapi.application.usecase.user.FindUserByNameUseCase;
import br.com.fiap.gastrohubapi.domain.entity.User;
import br.com.fiap.gastrohubapi.application.usecase.user.input.NewUserDTO;
import br.com.fiap.gastrohubapi.presentation.dto.response.UserDTO;
import br.com.fiap.gastrohubapi.presentation.mapper.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final FindUserByIdUseCase findUserByIdUseCase;
    private final FindUserByNameUseCase findUserByNameUseCase;
    private final UserMapper userMapper;

    public UserController(UserGateway userGateway, UserMapper userMapper) {
        this.createUserUseCase = CreateUserUseCase.create(userGateway);
        this.findUserByIdUseCase = FindUserByIdUseCase.create(userGateway);
        this.findUserByNameUseCase = FindUserByNameUseCase.create(userGateway);
        this.userMapper = userMapper;
    }

    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody NewUserDTO newUserDTO) {
        User user = this.createUserUseCase.run(newUserDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userMapper.toResponseDTO(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable UUID id) {
        User user = this.findUserByIdUseCase.run(id);
        return ResponseEntity.ok(this.userMapper.toResponseDTO(user));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<UserDTO> findByName(@PathVariable String name) {
        User user = this.findUserByNameUseCase.run(name);
        return ResponseEntity.ok(this.userMapper.toResponseDTO(user));
    }
}
