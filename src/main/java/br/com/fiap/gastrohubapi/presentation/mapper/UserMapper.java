package br.com.fiap.gastrohubapi.presentation.mapper;


import br.com.fiap.gastrohubapi.domain.entity.User;
import br.com.fiap.gastrohubapi.presentation.dto.response.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toResponseDTO(User user) {
        if (user == null) {
            return null;
        }

        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getUserType()
        );
    }
}