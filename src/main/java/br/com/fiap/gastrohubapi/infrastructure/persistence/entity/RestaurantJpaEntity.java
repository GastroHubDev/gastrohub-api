package br.com.fiap.gastrohubapi.infrastructure.persistence.entity;

import br.com.fiap.gastrohubapi.domain.entity.Restaurant;
import br.com.fiap.gastrohubapi.domain.enums.KitchenType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "restaurant")
public class RestaurantJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private KitchenType kitchenType;

    @Column(nullable = false)
    private String openingHours;

    @Column(nullable = false)
    private UUID restaurantOwnerId;

    protected RestaurantJpaEntity() {
    }

    public RestaurantJpaEntity(UUID id, String name, String address, KitchenType kitchenType, String openingHours, UUID restaurantOwnerId) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.kitchenType = kitchenType;
        this.openingHours = openingHours;
        this.restaurantOwnerId = restaurantOwnerId;
    }

    public static RestaurantJpaEntity fromDomain(Restaurant restaurant) {
        return new RestaurantJpaEntity(
            restaurant.getId(),
            restaurant.getName(),
            restaurant.getAddress(),
            restaurant.getKitchenType(),
            restaurant.getOpeningHours(),
            restaurant.getRestaurantOwnerId()
        );
    }

    public Restaurant toDomain() {
        return Restaurant.create(
                id,
                name,
                address,
                kitchenType,
                openingHours,
                restaurantOwnerId);
    }
}
