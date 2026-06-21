package br.com.fiap.gastrohubapi.presentation.controller;

import br.com.fiap.gastrohubapi.application.gateway.DataStorageSource;
import br.com.fiap.gastrohubapi.application.gateway.RestaurantGateway;

public class RestaurantController {
    private final DataStorageSource dataStorageSource;

    private RestaurantController(DataStorageSource dataStorageSource) {
        this.dataStorageSource = dataStorageSource;
    }

    public static RestaurantController create(DataStorageSource dataStorageSource) {
        return new RestaurantController(dataStorageSource);
    }

}
