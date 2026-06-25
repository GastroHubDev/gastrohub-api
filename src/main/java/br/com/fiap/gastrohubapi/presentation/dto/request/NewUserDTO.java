package br.com.fiap.gastrohubapi.presentation.dto.request;

import br.com.fiap.gastrohubapi.domain.entity.UserType;

import java.util.UUID;

public record NewUserDTO (UUID id, String name, String email, UserType userType, String password ){
}
