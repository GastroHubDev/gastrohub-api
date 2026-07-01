package br.com.fiap.gastrohubapi.application.usecase.user.input;

import br.com.fiap.gastrohubapi.domain.entity.UserType;

import java.util.UUID;

public record UpdateUserDTO(UUID id, String name, String email, UserType userType, String password ) {
}
