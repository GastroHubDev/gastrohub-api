package br.com.fiap.gastrohubapi.application.usecase.user.input;

public record NewUserDTO( String name, String email, Long userTypeId, String password ){
}
