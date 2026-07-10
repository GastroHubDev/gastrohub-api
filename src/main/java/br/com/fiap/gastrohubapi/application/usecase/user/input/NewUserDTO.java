package br.com.fiap.gastrohubapi.application.usecase.user.input;

import java.util.UUID;

public record NewUserDTO( String name, String email, UUID userTypeId, String password ){
}
