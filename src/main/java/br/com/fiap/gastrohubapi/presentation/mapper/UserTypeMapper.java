package br.com.fiap.gastrohubapi.presentation.mapper;

import br.com.fiap.gastrohubapi.domain.entity.UserType;
import br.com.fiap.gastrohubapi.presentation.dto.response.UserTypeResponse;

import java.util.List;

public class UserTypeMapper {

    private UserTypeMapper() {
    }

    public static UserTypeResponse toResponse(UserType userType) {
        return new UserTypeResponse(
                userType.getId(),
                userType.getName(),
                userType.getBaseCategory()
        );
    }

    public static List<UserTypeResponse> toResponseList(List<UserType> userTypes) {
        return userTypes.stream()
                .map(UserTypeMapper::toResponse)
                .toList();
    }
}