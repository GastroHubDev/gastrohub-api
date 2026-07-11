package br.com.fiap.gastrohubapi.presentation.mapper;

import br.com.fiap.gastrohubapi.domain.enums.BaseCategory;
import br.com.fiap.gastrohubapi.domain.entity.User;
import br.com.fiap.gastrohubapi.domain.entity.UserType;
import br.com.fiap.gastrohubapi.presentation.dto.response.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserMapperTest {
    private static final UUID TYPE_ID_1 = UUID.fromString("11111111-1111-1111-1111-111111111111");


    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        // O Mapper não tem injeção de dependências, então podemos instanciá-lo diretamente!
        userMapper = new UserMapper();
    }

    // -------------------------------------------------------------
    // TESTES DE MAPEAMENTO ÚNICO (toResponseDTO)
    // -------------------------------------------------------------

    @Test
    void shouldMapToUserResponseWhenUserIsValid() {
        // Arrange
        UUID userId = UUID.randomUUID();

        // Mockamos as entidades para não disparar as validações do Domínio
        User mockUser = mock(User.class);
        UserType mockUserType = mock(UserType.class);

        // Configuramos o que os getters vão devolver
        when(mockUser.getId()).thenReturn(userId);
        when(mockUser.getName()).thenReturn("Joao");
        when(mockUser.getEmail()).thenReturn("Joao@test.com");
        when(mockUser.getUserType()).thenReturn(mockUserType);

        when(mockUserType.getId()).thenReturn(TYPE_ID_1);
        when(mockUserType.getName()).thenReturn("CLIENT");
        when(mockUserType.getBaseCategory()).thenReturn(BaseCategory.CLIENT);

        // Act
        UserResponse response = userMapper.toResponseDTO(mockUser);

        // Assert
        assertNotNull(response);
        assertEquals(userId, response.id());
        assertEquals("Joao", response.name());
        assertEquals("Joao@test.com", response.email());

        assertNotNull(response.userType());
        assertEquals(TYPE_ID_1, response.userType().id());
        assertEquals("CLIENT", response.userType().name());
        assertEquals(BaseCategory.CLIENT, response.userType().baseCategory());
    }

    @Test
    void shouldReturnNullWhenUserIsNull() {
        // Act
        UserResponse response = userMapper.toResponseDTO(null);

        // Assert
        assertNull(response);
    }

    @Test
    void shouldMapUserWithoutUserTypeSuccessfully() {
        // Arrange
        UUID userId = UUID.randomUUID();
        User mockUser = mock(User.class);

        when(mockUser.getId()).thenReturn(userId);
        when(mockUser.getName()).thenReturn("Joao");
        when(mockUser.getEmail()).thenReturn("Joao@test.com");
        when(mockUser.getUserType()).thenReturn(null); // Simulando a ausência do UserType

        // Act
        UserResponse response = userMapper.toResponseDTO(mockUser);

        // Assert
        assertNotNull(response);
        assertEquals(userId, response.id());
        assertEquals("Joao", response.name());
        assertNull(response.userType()); // Garante que não estourou NullPointerException
    }

    // -------------------------------------------------------------
    // TESTES DE MAPEAMENTO DE LISTA (toResponseDTOList)
    // -------------------------------------------------------------

    @Test
    void shouldReturnMappedListWhenInputListIsValid() {
        // Arrange
        User mockUser = mock(User.class);
        when(mockUser.getId()).thenReturn(UUID.randomUUID());
        when(mockUser.getName()).thenReturn("Joao");
        // O restante pode ser nulo ou vazio, queremos apenas garantir que a lista itera

        List<User> userList = List.of(mockUser, mockUser); // Lista com 2 itens

        // Act
        List<UserResponse> responseList = userMapper.toResponseDTOList(userList);

        // Assert
        assertNotNull(responseList);
        assertEquals(2, responseList.size());
        assertEquals("Joao", responseList.get(0).name());
    }

    @Test
    void shouldReturnEmptyListWhenInputListIsNull() {
        // Act
        List<UserResponse> responseList = userMapper.toResponseDTOList(null);

        // Assert
        assertNotNull(responseList);
        assertTrue(responseList.isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenInputListIsEmpty() {
        // Act
        List<UserResponse> responseList = userMapper.toResponseDTOList(List.of());

        // Assert
        assertNotNull(responseList);
        assertTrue(responseList.isEmpty());
    }
}