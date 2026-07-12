package br.com.fiap.gastrohubapi.presentation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateUserRequest(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotNull UUID userTypeId,
        @NotBlank String password
) {}
