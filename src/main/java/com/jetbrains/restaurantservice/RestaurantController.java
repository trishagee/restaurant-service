package com.jetbrains.restaurantservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RestaurantController {
	private final RestaurantRepository restaurantRepository;

	public RestaurantController(RestaurantRepository restaurantRepository) {
		this.restaurantRepository = restaurantRepository;
	}

	@GetMapping("/restaurant")
	public Restaurant restaurant() {
		return new Restaurant("Dalia's", "", 0, 0, List.of());
	}

	@GetMapping("/restaurants")
	public List<Restaurant> getRestaurants() {
		return restaurantRepository.findAll();
	}
}
