package com.jetbrains.restaurantservice;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RestaurantController {
    private final RestaurantRepository restaurantRepository;

    public RestaurantController(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping("/restaurants")
    public List<Restaurant> getRestaurants() {
        return restaurantRepository.findAll();
    }

    @GetMapping("/restaurants/{id}")
    public Restaurant restaurant(@PathVariable String id) {
        return restaurantRepository.findById(id).orElseThrow(() -> new RestaurantNotFoundException(id));
    }

    @DeleteMapping("/restaurants/{id}")
    public void deleteRestaurant(@PathVariable String id) {
        if (!restaurantExists(id)) {
            throw new RestaurantNotFoundException(id);
        }

        restaurantRepository.deleteById(id);
    }

    @PostMapping("/restaurants")
    public void saveRestaurant(@RequestBody Restaurant restaurant) {
        restaurantRepository.save(restaurant);
    }

    /**
     * This method will completely replace the existing restaurant with <code>id</code> with <code>updatedRestaurant</code>.
     * @param updatedRestaurant this needs to have the full details for the restaurant you're updating
     * @param id the ID of the restaurant you are updating. This will be used to locate and update the restaurant to be
     *           changed, NOT the ID in the updatedRestaurant object
     */
    @PutMapping("/restaurants/{id}")
    Restaurant replaceRestaurant(@RequestBody Restaurant updatedRestaurant, @PathVariable String id) {
        if (!restaurantExists(id)) {
            throw new RestaurantNotFoundException(id);
        }

        return restaurantRepository.save(updatedRestaurant);
    }

    private boolean restaurantExists(final String id) {
        return restaurantRepository.existsById(id);
    }
}
