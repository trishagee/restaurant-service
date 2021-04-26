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
