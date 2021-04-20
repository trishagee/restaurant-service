package com.jetbrains.restaurantservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
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
    @DisplayName("Should return a 404 when the restaurant is not there")
    void shouldReturnA404WhenTheRestaurantIsNotThere() throws Exception {
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
    @DisplayName("Should give a 404 if deleting a restaurant that is not there")
    void shouldGiveA404IfDeletingARestaurantThatIsNotThere() throws Exception {
        // given
        insertFourRestaurants();
        String restaurantId = "5647856473";

        // when
        this.mockMvc.perform(delete("/restaurants/" + restaurantId))
                    .andExpect(status().isNotFound());
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

    @Test
    @DisplayName("Should be able to edit a restaurant details")
    void shouldBeAbleToEditARestaurantDetails() throws Exception {
        // given
        insertFourRestaurants();

        Restaurant updatedDetails = new Restaurant("2", "Helen's Place", "Leeky kitchen", 33, 11, List.of());

        // when
        mockMvc.perform(put("/restaurants/2")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(updatedDetails)))
               .andExpect(status().isOk());

        // then
        Optional<Restaurant> actualRestaurant = restaurantRepository.findById("2");
        Assertions.assertTrue(actualRestaurant.isPresent());
        Assertions.assertEquals(updatedDetails, actualRestaurant.get());
    }


    @Test
    @DisplayName("Should only change one field when passing in just that field")
    @Disabled("We think that 'put' should do a full replace of the object. We think additional methods might be needed for, e.g., opening hours")
    void shouldOnlyChangeOneFieldWhenPassingInJustThatField() throws Exception {
        // TODO: what's the actual expected behaviour? Should it be OK to pass in a single field?
        // given
        insertFourRestaurants();

        // when
        mockMvc.perform(put("/restaurants/3")
                                .contentType("application/json")
                                .content("{\"indoorCapacity\":0}"))
               .andExpect(status().isOk());

        // then
        Optional<Restaurant> actualRestaurant = restaurantRepository.findById("3");
        Assertions.assertTrue(actualRestaurant.isPresent());

        Restaurant expected = new Restaurant("3", "Trisha's", "Sevilla", 0, 8, List.of());
        Assertions.assertEquals(expected, actualRestaurant.get());
    }

    @Test
    @DisplayName("Should return a 404 if trying to update a restaurant that does not exist")
    void shouldReturnA404IfTryingToUpdateARestaurantThatDoesNotExist() throws Exception {
        Restaurant updatedDetails = new Restaurant("657486547", "Helen's Place", "Leeky kitchen", 33, 11, List.of());

        // expect
        mockMvc.perform(put("/restaurants/657486547")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(updatedDetails)))
               .andExpect(status().isNotFound());
    }

    private void insertFourRestaurants() {
        restaurantRepository.deleteAll();

        Restaurant dalia = new Restaurant("1", "Dalia's", "Rochester", 20, 50, List.of());
        Restaurant helen = new Restaurant("2", "Helen's", "Leek", 30, 10, List.of());
        Restaurant trisha = new Restaurant("3", "Trisha's", "Sevilla", 12, 8, List.of());
        Restaurant mala = new Restaurant("4", "Mala's", "New Delhi", 25, 0, List.of());

        restaurantRepository.saveAll(List.of(dalia, helen, trisha, mala));
    }

    // TODO: figure out the best way to add seed data

    // Restaurant Admins:
    // {id: admin_id,
    //  name: bob
    //  password:
    //  email: ,
    //  restaurant_id: int}
}