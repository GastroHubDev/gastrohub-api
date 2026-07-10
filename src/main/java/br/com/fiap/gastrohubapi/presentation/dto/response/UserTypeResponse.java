package br.com.fiap.gastrohubapi.presentation.dto.response;

import br.com.fiap.gastrohubapi.domain.enums.BaseCategory;

import java.util.UUID;

public record UserTypeResponse(
        UUID id,
        String name,
        BaseCategory baseCategory
) {
}
