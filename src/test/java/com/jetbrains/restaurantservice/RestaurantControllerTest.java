package com.jetbrains.restaurantservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class RestaurantControllerTest {
    private final Restaurant restaurant1 = new Restaurant("1", "add1", 3, 5, List.of());
    private final Restaurant restaurant2 = new Restaurant("2", "add2", 7, 11, List.of());

    @Test
    @DisplayName("Should fetch all restaurants from the repository")
    void shouldFetchAllRestaurantsFromTheRepository(@Mock RestaurantRepository restaurantRepository) {
        // given
        List<Restaurant> expectedRestaurants = List.of(this.restaurant1, restaurant2);
        Mockito.when(restaurantRepository.findAll())
               .then(invocationOnMock -> expectedRestaurants);
        RestaurantController restaurantController = new RestaurantController(restaurantRepository);

        // when
        List<Restaurant> actualRestaurants = restaurantController.getRestaurants();

        // then
        Assertions.assertEquals(expectedRestaurants, actualRestaurants);
    }


    @Test
    @DisplayName("Should get a restaurant by ID from the repository")
    void shouldGetARestaurantByIdFromTheRepository(@Mock RestaurantRepository restaurantRepository) {
        // given
        List<Restaurant> expectedRestaurants = List.of(this.restaurant1, restaurant2);
        Mockito.when(restaurantRepository.findAll())
               .then(invocationOnMock -> expectedRestaurants);
        RestaurantController restaurantController = new RestaurantController(restaurantRepository);

        // when
        List<Restaurant> actualRestaurants = restaurantController.getRestaurants();

        // then
        Assertions.assertEquals(expectedRestaurants, actualRestaurants);
    }

    @Test
    @DisplayName("Should ask the repository to delete a restaurant by ID")
    void shouldAskTheRepositoryToDeleteARestaurantById(@Mock RestaurantRepository restaurantRepository) {
        // given
        String restaurantId = "3";

        RestaurantController restaurantController = new RestaurantController(restaurantRepository);

        // when
        restaurantController.deleteRestaurant(restaurantId);

        // then
        verify(restaurantRepository).deleteById(restaurantId);
        verifyNoMoreInteractions(restaurantRepository);
    }

}