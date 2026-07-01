package br.com.fiap.gastrohubapi.presentation.mapper;

import br.com.fiap.gastrohubapi.domain.entity.User;
import br.com.fiap.gastrohubapi.presentation.dto.response.UserResponse;
import br.com.fiap.gastrohubapi.presentation.dto.response.UserTypeResponse; // Não esqueça deste import!
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public UserResponse toResponseDTO(User user) {
        if (user == null) {
            return null;
        }

        UserTypeResponse typeResponse = null;
        if (user.getUserType() != null) {
            typeResponse = new UserTypeResponse(
                    user.getUserType().getId(),
                    user.getUserType().getName(),
                    user.getUserType().getBaseCategory()
            );
        }

        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                typeResponse
        );
    }

    public List<UserResponse> toResponseDTOList(List<User> users) {
        if (users == null) return List.of();

        return users.stream()
                .map(this::toResponseDTO)
                .toList();
    }
}