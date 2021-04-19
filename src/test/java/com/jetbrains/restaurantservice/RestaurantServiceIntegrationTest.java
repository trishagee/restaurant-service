package com.jetbrains.restaurantservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RestaurantServiceIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should return a list of all restaurants in the database")
    void shouldReturnAListOfAllRestaurantsInTheDatabase() throws Exception {
        // Given:
        insertFourRestaurants();

        this.mockMvc.perform(get("/restaurants"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("""
                                                      [{
                                                               "id" : "1",
                                                               "name" : "Dalia's",
                                                               "address" : "Rochester",
                                                               "indoorCapacity" : 20,
                                                               "outdoorCapacity" : 50,
                                                               "openingHours" : [ ]
                                                       },
                                                       {
                                                               "id" : "2",
                                                               "name" : "Helen's",
                                                               "address" : "Leek",
                                                               "indoorCapacity" : 30,
                                                               "outdoorCapacity" : 10,
                                                               "openingHours" : [ ]
                                                       },
                                                       {
                                                               "id" : "3",
                                                               "name" : "Trisha's",
                                                               "address" : "Sevilla",
                                                               "indoorCapacity" : 12,
                                                               "outdoorCapacity" : 8,
                                                               "openingHours" : [ ]
                                                       },
                                                       {
                                                               "id" : "4",
                                                               "name" : "Mala's",
                                                               "address" : "New Delhi",
                                                               "indoorCapacity" : 25,
                                                               "outdoorCapacity" : 0,
                                                               "openingHours" : [ ]
                                                       }
                                                       ]
                                                      """));
    }


    @Test
    void shouldGetARestaurantById() throws Exception {
        insertFourRestaurants();

        this.mockMvc.perform(get("/restaurants/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("""
                                                      {"id" : "1",
                                                       "name" : "Dalia's",
                                                       "address" : "Rochester",
                                                       "indoorCapacity" : 20,
                                                       "outdoorCapacity" : 50,
                                                       "openingHours" : [ ]
                                                      }
                                                      """));
    }

    @Test
    @DisplayName("Should return a what when the restaurant is not there")
    void shouldReturnAWhatWhenTheRestaurantIsNotThere() throws Exception {
        this.mockMvc.perform(get("/restaurants/785697865"))
                    .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should delete a restaurant by ID")
    void shouldDeleteARestaurantById() throws Exception {
        // given
        insertFourRestaurants();
        String restaurantId = "3";
        // check the restaurant really is there
        this.mockMvc.perform(get("/restaurants/" + restaurantId))
                    .andExpect(status().isOk());

        // when
        this.mockMvc.perform(delete("/restaurants/" + restaurantId))
                    .andExpect(status().isOk());

        // then
        this.mockMvc.perform(get("/restaurants/" + restaurantId))
                    .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should not error if deleting a restaurant that is not there")
    void shouldNotErrorIfDeletingARestaurantThatIsNotThere() throws Exception {
        // given
        insertFourRestaurants();
        String restaurantId = "5647856473";

        // when
        this.mockMvc.perform(delete("/restaurants/" + restaurantId))
                    .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should create a new restaurant")
    void shouldCreateANewRestaurant() throws Exception {
        String restaurantId = "10";
        Restaurant newRestaurant = new Restaurant(restaurantId, "New Restaurant", "London", 7, 6473, List.of());

        // when
        mockMvc.perform(post("/restaurants")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(newRestaurant)))
               .andExpect(status().isOk());

        // then
        Optional<Restaurant> actualRestaurant = restaurantRepository.findById(restaurantId);
        Assertions.assertTrue(actualRestaurant.isPresent());
        Assertions.assertEquals(newRestaurant, actualRestaurant.get());
    }

    private void insertFourRestaurants() {
        restaurantRepository.deleteAll();

        Restaurant dalia = new Restaurant("1", "Dalia's", "Rochester", 20, 50, List.of());
        Restaurant helen = new Restaurant("2", "Helen's", "Leek", 30, 10, List.of());
        Restaurant trisha = new Restaurant("3", "Trisha's", "Sevilla", 12, 8, List.of());
        Restaurant mala = new Restaurant("4", "Mala's", "New Delhi", 25, 0, List.of());

        restaurantRepository.saveAll(List.of(dalia, helen, trisha, mala));
    }

    // TODO: update a restaurant's details

    // TODO: figure out the best way to add seed data

    // Restaurant Admins:
    // {id: admin_id,
    //  name: bob
    //  password:
    //  email: ,
    //  restaurant_id: int}
}