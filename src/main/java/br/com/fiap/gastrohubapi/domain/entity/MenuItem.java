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

    public MenuItem(UUID id,
                    String name,
                    String description,
                    BigDecimal price,
                    boolean onlyInRestaurant,
                    String photoPath,
                    UUID restaurantId)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.onlyInRestaurant = onlyInRestaurant;
        this.photoPath = photoPath;
        this.restaurantId = restaurantId;
    }

    public UUID getId() {return id;}
    public String getName() {return name;}
    public String getDescription() {return description;}
    public BigDecimal getPrice() {return price;}
    public boolean isOnlyInRestaurant() {return onlyInRestaurant;}
    public String getPhotoPath() {return photoPath;}
    public UUID getRestaurantId() {return restaurantId;}
}
