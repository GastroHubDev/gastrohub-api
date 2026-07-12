package br.com.fiap.gastrohubapi.application.usecase.user.input;

import java.util.UUID;

public record UpdateUserDTO(UUID id, String name, String email, UUID userTypeId, String password ) {
}
