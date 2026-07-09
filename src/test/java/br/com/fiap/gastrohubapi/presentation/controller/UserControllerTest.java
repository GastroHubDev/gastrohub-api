package br.com.fiap.gastrohubapi.presentation.controller;

import br.com.fiap.gastrohubapi.application.usecase.user.*;
import br.com.fiap.gastrohubapi.application.usecase.user.input.NewUserDTO;
import br.com.fiap.gastrohubapi.application.usecase.user.input.UpdateUserDTO;
import br.com.fiap.gastrohubapi.domain.entity.User;
import br.com.fiap.gastrohubapi.presentation.dto.request.CreateUserRequest;
import br.com.fiap.gastrohubapi.presentation.dto.request.UpdateUserRequest;
import br.com.fiap.gastrohubapi.presentation.dto.response.UserResponse;
import br.com.fiap.gastrohubapi.presentation.dto.response.UserTypeResponse;
import br.com.fiap.gastrohubapi.presentation.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static br.com.fiap.gastrohubapi.domain.entity.BaseCategory.CLIENT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock private CreateUserUseCase createUserUseCase;
    @Mock private FindUserByIdUseCase findUserByIdUseCase;
    @Mock private FindUserByNameUseCase findUserByNameUseCase;
    @Mock private FindUserByEmailUseCase findUserByEmailUseCase;
    @Mock private UpdateUserUseCase updateUserUseCase;
    @Mock private FindAllUsersUseCase findAllUsersUseCase;
    @Mock private DeleteUserUseCase deleteUserUseCase;
    @Mock private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    private static final Long USER_TYPE_ID = 1L;

    private UUID userId;
    private User mockUser;
    private UserResponse mockUserResponse;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        mockUser = mock(User.class);

        mockUserResponse = new UserResponse(userId, "Joao", "Joao@test.com", new UserTypeResponse(1L, "CLIENT", CLIENT));
    }

    @Test
    void shouldCreateUserAndReturn201Created() {
        CreateUserRequest request = new CreateUserRequest("Joao", "Joao@test.com", USER_TYPE_ID, "password123");

        when(createUserUseCase.run(any(NewUserDTO.class))).thenReturn(mockUser);
        when(userMapper.toResponseDTO(mockUser)).thenReturn(mockUserResponse);

        ResponseEntity<UserResponse> response = userController.create(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(mockUserResponse, response.getBody());

        verify(createUserUseCase, times(1)).run(any(NewUserDTO.class));
        verify(userMapper, times(1)).toResponseDTO(mockUser);
    }

    @Test
    void shouldFindUserByIdAndReturn200Ok() {
        when(findUserByIdUseCase.run(userId)).thenReturn(mockUser);
        when(userMapper.toResponseDTO(mockUser)).thenReturn(mockUserResponse);

        ResponseEntity<UserResponse> response = userController.findById(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUserResponse, response.getBody());

        verify(findUserByIdUseCase, times(1)).run(userId);
    }

    @Test
    void shouldFindUsersByNameAndReturn200Ok() {
        String searchName = "Joao";
        List<User> userList = List.of(mockUser);
        List<UserResponse> responseList = List.of(mockUserResponse);

        when(findUserByNameUseCase.run(searchName)).thenReturn(userList);
        when(userMapper.toResponseDTOList(userList)).thenReturn(responseList);

        ResponseEntity<List<UserResponse>> response = userController.findByName(searchName);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseList, response.getBody());

        verify(findUserByNameUseCase, times(1)).run(searchName);
    }

    @Test
    void shouldFindUserByEmailAndReturn200Ok() {
        // Arrange
        String email = "Joao@test.com";
        when(findUserByEmailUseCase.execute(email)).thenReturn(mockUser);
        when(userMapper.toResponseDTO(mockUser)).thenReturn(mockUserResponse);

        // Act
        ResponseEntity<UserResponse> response = userController.findByEmail(email);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUserResponse, response.getBody());

        verify(findUserByEmailUseCase, times(1)).execute(email);
    }

    @Test
    void shouldUpdateUserAndReturn200Ok() {
        // Arrange
        UpdateUserRequest request = new UpdateUserRequest("Joao Atualizado", "novo@test.com", USER_TYPE_ID, "newpass");

        when(updateUserUseCase.execute(any(UpdateUserDTO.class))).thenReturn(mockUser);
        when(userMapper.toResponseDTO(mockUser)).thenReturn(mockUserResponse);

        // Act
        ResponseEntity<UserResponse> response = userController.update(userId, request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUserResponse, response.getBody());

        verify(updateUserUseCase, times(1)).execute(any(UpdateUserDTO.class));
    }

    @Test
    void shouldFindAllUsersAndReturn200Ok() {
        // Arrange
        List<User> userList = List.of(mockUser);
        List<UserResponse> responseList = List.of(mockUserResponse);

        when(findAllUsersUseCase.execute()).thenReturn(userList);
        when(userMapper.toResponseDTOList(userList)).thenReturn(responseList);

        // Act
        ResponseEntity<List<UserResponse>> response = userController.findAll();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseList, response.getBody());

        verify(findAllUsersUseCase, times(1)).execute();
    }

    @Test
    void shouldDeleteUserAndReturn204NoContent() {
        // Arrange
        doNothing().when(deleteUserUseCase).execute(userId);

        // Act
        ResponseEntity<Void> response = userController.delete(userId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(deleteUserUseCase, times(1)).execute(userId);
    }
}