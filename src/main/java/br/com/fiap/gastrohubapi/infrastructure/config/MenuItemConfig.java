package br.com.fiap.gastrohubapi.infrastructure.config;

import br.com.fiap.gastrohubapi.application.gateway.MenuItemGateway;
import br.com.fiap.gastrohubapi.application.usecase.menuitem.*;
import br.com.fiap.gastrohubapi.infrastructure.persistence.gateway.MenuItemGatewayImpl;
import br.com.fiap.gastrohubapi.infrastructure.persistence.repository.MenuItemRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MenuItemConfig {

    @Bean
    public MenuItemGateway menuItemGateway(MenuItemRepository repository) {
        return new MenuItemGatewayImpl(repository);
    }

    @Bean
    public CreateMenuItemUseCase createMenuItemUseCase(MenuItemGateway gateway) {
        return new CreateMenuItemUseCase(gateway);
    }

    @Bean
    public FindMenuItemByIdUseCase findMenuItemByIdUseCase(MenuItemGateway gateway) {
        return new FindMenuItemByIdUseCase(gateway);
    }

    @Bean
    public FindAllMenuItemsUseCase findAllMenuItemsUseCase(MenuItemGateway gateway) {
        return new FindAllMenuItemsUseCase(gateway);
    }

    @Bean
    public FindMenuItemsByRestaurantUseCase findMenuItemsByRestaurantUseCase(MenuItemGateway gateway) {
        return new FindMenuItemsByRestaurantUseCase(gateway);
    }

    @Bean
    public UpdateMenuItemUseCase updateMenuItemUseCase(MenuItemGateway gateway) {
        return new UpdateMenuItemUseCase(gateway);
    }

    @Bean
    public DeleteMenuItemUseCase deleteMenuItemUseCase(MenuItemGateway gateway) {
        return new DeleteMenuItemUseCase(gateway);
    }
}