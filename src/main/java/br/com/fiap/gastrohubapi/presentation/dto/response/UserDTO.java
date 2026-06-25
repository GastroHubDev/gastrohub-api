package br.com.fiap.gastrohubapi.presentation.dto.response;

import br.com.fiap.gastrohubapi.domain.entity.UserType;

import java.util.UUID;

public record UserDTO(UUID id, String name, String email, UserType userType) {


}
