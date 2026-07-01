package br.com.fiap.gastrohubapi.presentation.dto.request;

import br.com.fiap.gastrohubapi.domain.entity.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateUserRequest(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotNull UserType userType,
        @NotBlank String password
) {}