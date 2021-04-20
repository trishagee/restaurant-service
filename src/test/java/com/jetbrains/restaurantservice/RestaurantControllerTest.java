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
        // Stubbing: tell Mockito to return this list of expected restaurants if it's asked to find restaurants
        Mockito.when(restaurantRepository.findAll())
               .thenReturn(expectedRestaurants);
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
        // Stubbing: tell Mockito that the repo should say this restaurant exists
        Mockito.when(restaurantRepository.existsById(restaurantId))
               .thenReturn(true);

        // when
        restaurantController.deleteRestaurant(restaurantId);

        // then
        verify(restaurantRepository).deleteById(restaurantId);
    }

    @Test
    @DisplayName("Should error if trying to delete a restaurant that does not exist")
    void shouldErrorIfTryingToDeleteARestaurantThatDoesNotExist(@Mock RestaurantRepository restaurantRepository) {
        // given
        RestaurantController restaurantController = new RestaurantController(restaurantRepository);

        // when
        Assertions.assertThrows(RestaurantNotFoundException.class,
                                () -> restaurantController.deleteRestaurant("564756437"));
    }

    @Test
    @DisplayName("Should ask the repository to save a restaurant")
    void shouldAskTheRepositoryToSaveARestaurant(@Mock RestaurantRepository restaurantRepository) {
        // given
        RestaurantController restaurantController = new RestaurantController(restaurantRepository);

        // when
        restaurantController.saveRestaurant(restaurant1);

        // then
        verify(restaurantRepository).save(restaurant1);
        verifyNoMoreInteractions(restaurantRepository);
    }

}