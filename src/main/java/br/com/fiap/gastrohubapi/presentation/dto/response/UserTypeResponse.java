package br.com.fiap.gastrohubapi.presentation.dto.response;

import br.com.fiap.gastrohubapi.domain.entity.BaseCategory;

public record UserTypeResponse(
        Long id,
        String name,
        BaseCategory baseCategory
) {
}