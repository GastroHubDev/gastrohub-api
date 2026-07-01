package br.com.fiap.gastrohubapi.domain.entity;

import java.math.BigDecimal;
import java.util.UUID;

public class MenuItem {

    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private boolean onlyInRestaurant;
    private String photoPath;
    private UUID restaurantId;

    private MenuItem() {}

    public static MenuItem create(String name, String description, BigDecimal price,
                                   boolean onlyInRestaurant, String photoPath, UUID restaurantId) {
        validateName(name);
        validateDescription(description);
        validatePrice(price);
        validateRestaurantId(restaurantId);

        MenuItem item = new MenuItem();
        item.name = name;
        item.description = description;
        item.price = price;
        item.onlyInRestaurant = onlyInRestaurant;
        item.photoPath = photoPath;
        item.restaurantId = restaurantId;
        return item;
    }

    public static MenuItem restore(UUID id, String name, String description, BigDecimal price,
                                    boolean onlyInRestaurant, String photoPath, UUID restaurantId) {
        validateName(name);
        validateDescription(description);
        validatePrice(price);
        validateRestaurantId(restaurantId);

        MenuItem item = new MenuItem();
        item.id = id;
        item.name = name;
        item.description = description;
        item.price = price;
        item.onlyInRestaurant = onlyInRestaurant;
        item.photoPath = photoPath;
        item.restaurantId = restaurantId;
        return item;
    }

    private static void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("MenuItem name cannot be null or empty.");
        }
    }

    private static void validateDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("MenuItem description cannot be null or empty.");
        }
    }

    private static void validatePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("MenuItem price must be greater than zero.");
        }
    }

    private static void validateRestaurantId(UUID restaurantId) {
        if (restaurantId == null) {
            throw new IllegalArgumentException("MenuItem restaurantId cannot be null.");
        }
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public boolean isOnlyInRestaurant() { return onlyInRestaurant; }
    public String getPhotoPath() { return photoPath; }
    public UUID getRestaurantId() { return restaurantId; }
}
