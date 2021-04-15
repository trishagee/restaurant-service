package com.jetbrains.restaurantservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

	@GetMapping("/restaurants")
	public List<Restaurant> getRestaurants() {
		return restaurantRepository.findAll();
	}
}
