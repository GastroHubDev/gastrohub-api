package br.com.fiap.gastrohubapi.application.gateway;

import br.com.fiap.gastrohubapi.presentation.dto.response.UserDTO;

public interface IUserDataSource {
    UserDTO getUserByName(String name);

    UserDTO saveUser(UserDTO userToSave);
}
