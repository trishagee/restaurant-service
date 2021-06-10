package com.jetbrains.restaurantservice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(MongoDBContainerExtension.class)
class RestaurantRepositoryTest {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Test
    @DisplayName("Should save and retrieve a restaurant from the database")
    void shouldSaveAndRetrieveARestaurantFromTheDatabase() {
        Restaurant blossom = new Restaurant("Blossom", null, 0, Set.of());

        restaurantRepository.save(blossom);

        Optional<Restaurant> restaurant = restaurantRepository.findById(blossom.id);
        assertTrue(restaurant.isPresent());
        assertEquals(blossom, restaurant.get());
    }
}