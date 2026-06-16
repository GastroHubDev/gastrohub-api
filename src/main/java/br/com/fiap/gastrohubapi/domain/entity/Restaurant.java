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
    private User restaurantOwner;


    public static Restaurant create(UUID id, String name, String address, KitchenType kitchenType, String openingHours, User restaurantOwner) {

        Restaurant restaurant = new Restaurant();
        restaurant.setId(id);
        restaurant.setName(name);
        restaurant.setAddress(address);
        restaurant.setKitchenType(kitchenType);
        restaurant.setOpeningHours(openingHours);
        restaurant.setRestaurantOwner(restaurantOwner);

        return restaurant;
    }

    public static Restaurant create(String name, String address, KitchenType kitchenType, String openingHours, User restaurantOwner) {

        Restaurant restaurant = new Restaurant();
        restaurant.setName(name);
        restaurant.setAddress(address);
        restaurant.setKitchenType(kitchenType);
        restaurant.setOpeningHours(openingHours);
        restaurant.setRestaurantOwner(restaurantOwner);

        return restaurant;
    }

    public static void validateRestaurantOwner(User restaurantOwner){
        if(restaurantOwner == null) {
            throw new IllegalArgumentException("OpeningHours cannot be null or empty");
        }

        // TODO
        // Adicionar verificação para não permitir usuários do tipo CLIENT serem donos de restaurante.
        // No momento estou sem o user implementado e não consigo obter seu tipo (OWNER ou CLIENT).
        // Use: UserTypeNotAllowedForRestaurantOwner
    }

    public static void validateId(UUID id){
        if(id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
    }

    public static void validateName(String name){
        if(name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
    }

    public static void validateAddress(String address){
        if(address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("address cannot be null or empty");
        }
    }

    public static void validateKitchenType(KitchenType kitchenType){
        if(kitchenType == null) {
            throw new IllegalArgumentException("KitchenType cannot be null");
        }
    }

    public static void validateOpeningHours(String openingHours){
        if(openingHours == null || openingHours.trim().isEmpty()) {
            throw new IllegalArgumentException("OpeningHours cannot be null or empty");
        }
    }

    public void setId(UUID id) {
        validateId(id);
        this.id = id;
    }

    public void setName(String name) {
        validateName(name);
        this.name = name;
    }

    public void setAddress(String address) {
        validateAddress(address);
        this.address = address;
    }

    public void setKitchenType(KitchenType kitchenType) {
        validateKitchenType(kitchenType);
        this.kitchenType = kitchenType;
    }

    public void setOpeningHours(String openingHours) {
        validateOpeningHours(openingHours);
        this.openingHours = openingHours;
    }

    public void setRestaurantOwner(User restaurantOwner) {
        validateRestaurantOwner(restaurantOwner);
        this.restaurantOwner = restaurantOwner;
    }
}