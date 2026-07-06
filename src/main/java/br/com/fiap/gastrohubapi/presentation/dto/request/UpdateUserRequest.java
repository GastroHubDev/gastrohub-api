package br.com.fiap.gastrohubapi.presentation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateUserRequest(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotNull Long userTypeId,
        @NotBlank String password
) {}
