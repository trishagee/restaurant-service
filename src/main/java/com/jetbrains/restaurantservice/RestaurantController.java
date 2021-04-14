package com.jetbrains.restaurantservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestaurantController {

	@GetMapping("/restaurant")
	public Restaurant restaurant() {
		return new Restaurant("Dalia's", "", 0, 0, List.of());
	}
}
