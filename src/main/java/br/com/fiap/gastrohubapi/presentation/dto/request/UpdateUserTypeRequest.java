package br.com.fiap.gastrohubapi.presentation.dto.request;

import br.com.fiap.gastrohubapi.domain.entity.BaseCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateUserTypeRequest(
        @NotBlank(message = "Name is required.")
        String name,

        @NotNull(message = "Base category is required.")
        BaseCategory baseCategory
) {
}