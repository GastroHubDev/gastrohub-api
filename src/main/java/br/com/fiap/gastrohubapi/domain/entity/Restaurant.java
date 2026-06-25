package br.com.fiap.gastrohubapi.domain.entity;

import br.com.fiap.gastrohubapi.domain.enums.KitchenType;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Restaurant {
    private UUID id;
    private String name;
    private String address;
    private KitchenType kitchenType;
    private String openingHours;
    private UUID restaurantOwnerId;


    private Restaurant() {}

    public static Restaurant create(UUID id, String name, String address, KitchenType kitchenType, String openingHours, UUID restaurantOwnerId) {

        Restaurant restaurant = new Restaurant();
        restaurant.setId(id);
        restaurant.setName(name);
        restaurant.setAddress(address);
        restaurant.setKitchenType(kitchenType);
        restaurant.setOpeningHours(openingHours);
        restaurant.setRestaurantOwnerId(restaurantOwnerId);

        return restaurant;
    }

    public static Restaurant create(String name, String address, KitchenType kitchenType, String openingHours, UUID restaurantOwnerId) {

        Restaurant restaurant = new Restaurant();
        restaurant.setName(name);
        restaurant.setAddress(address);
        restaurant.setKitchenType(kitchenType);
        restaurant.setOpeningHours(openingHours);
        restaurant.setRestaurantOwnerId(restaurantOwnerId);

        return restaurant;
    }

    public void update(String name, String address, KitchenType kitchenType, String openingHours, UUID restaurantOwnerId) {
        setName(name);
        setAddress(address);
        setKitchenType(kitchenType);
        setOpeningHours(openingHours);
        setRestaurantOwnerId(restaurantOwnerId);
    }

    private static void validateRestaurantOwnerId(UUID restaurantOwnerId){
        if(restaurantOwnerId == null) {
            throw new IllegalArgumentException("RestaurantOwnerId cannot be null");
        }
    }

    private static void validateId(UUID id){
        if(id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
    }

    private static void validateName(String name){
        if(name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
    }

    private static void validateAddress(String address){
        if(address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("address cannot be null or empty");
        }
    }

    private static void validateKitchenType(KitchenType kitchenType){
        if(kitchenType == null) {
            throw new IllegalArgumentException("KitchenType cannot be null");
        }
    }

    private static void validateOpeningHours(String openingHours){
        if(openingHours == null || openingHours.trim().isEmpty()) {
            throw new IllegalArgumentException("OpeningHours cannot be null or empty");
        }
    }

    private void setId(UUID id) {
        validateId(id);
        this.id = id;
    }

    private void setName(String name) {
        validateName(name);
        this.name = name;
    }

    private void setAddress(String address) {
        validateAddress(address);
        this.address = address;
    }

    private void setKitchenType(KitchenType kitchenType) {
        validateKitchenType(kitchenType);
        this.kitchenType = kitchenType;
    }

    private void setOpeningHours(String openingHours) {
        validateOpeningHours(openingHours);
        this.openingHours = openingHours;
    }

    private void setRestaurantOwnerId(UUID restaurantOwnerId) {
        validateRestaurantOwnerId(restaurantOwnerId);
        this.restaurantOwnerId = restaurantOwnerId;
    }
}