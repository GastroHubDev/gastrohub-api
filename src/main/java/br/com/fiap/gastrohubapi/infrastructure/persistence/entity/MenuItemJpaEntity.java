package br.com.fiap.gastrohubapi.infrastructure.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "menu_items")
public class MenuItemJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private boolean onlyInRestaurant;

    private String photoPath;

    @Column(nullable = false)
    private UUID restaurantId;

    public MenuItemJpaEntity() {}

    public MenuItemJpaEntity(UUID id, String name, String description,
                             BigDecimal price, boolean onlyInRestaurant,
                             String photoPath, UUID restaurantId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.onlyInRestaurant = onlyInRestaurant;
        this.photoPath = photoPath;
        this.restaurantId = restaurantId;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public boolean isOnlyInRestaurant() { return onlyInRestaurant; }
    public void setOnlyInRestaurant(boolean onlyInRestaurant) { this.onlyInRestaurant = onlyInRestaurant; }
    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }
    public UUID getRestaurantId() { return restaurantId; }
    public void setRestaurantId(UUID restaurantId) { this.restaurantId = restaurantId; }
}