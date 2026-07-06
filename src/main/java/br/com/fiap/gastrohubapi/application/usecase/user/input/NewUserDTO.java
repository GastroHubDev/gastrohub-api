package br.com.fiap.gastrohubapi.application.usecase.user.input;

import java.util.UUID;

public record NewUserDTO(UUID id, String name, String email, Long userTypeId, String password ){
}
