package com.jetbrains.restaurantservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RestaurantController {
	private final RestaurantRepository restaurantRepository;

	public RestaurantController(RestaurantRepository restaurantRepository) {
		this.restaurantRepository = restaurantRepository;
	}

	@GetMapping("/restaurants/{id}")
	public Restaurant restaurant(@PathVariable String id) {
		return restaurantRepository.findById(id).orElseThrow(() -> new RestaurantNotFoundException(id));
	}

	@DeleteMapping("/restaurants/{id}")
	public void deleteRestaurant(@PathVariable String id) {
		restaurantRepository.deleteById(id);
	}

	@GetMapping("/restaurants")
	public List<Restaurant> getRestaurants() {
		return restaurantRepository.findAll();
	}

	@PostMapping("/restaurants")
	public void saveRestaurant(@RequestBody Restaurant restaurant) {
		restaurantRepository.save(restaurant);
	}
}
