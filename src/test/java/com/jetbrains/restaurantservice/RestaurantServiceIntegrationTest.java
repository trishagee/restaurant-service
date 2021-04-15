package com.jetbrains.restaurantservice;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RestaurantServiceIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    void shouldGetAStubRestaurantFromTheRestaurantService() throws Exception {
        this.mockMvc.perform(get("/restaurant"))
                    .andExpect(status().isOk())
                    .andExpect(content().json("""
                                                      {"id":null,
                                                      "name":"Dalia's",
                                                      "address":"",
                                                      "indoorCapacity":0,
                                                      "outdoorCapacity":0,
                                                      "openingHours":[]}
                                                      """));
    }

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

    private void insertFourRestaurants() {
        restaurantRepository.deleteAll();

        Restaurant dalia = new Restaurant("1","Dalia's", "Rochester", 20, 50, List.of());
        Restaurant helen = new Restaurant("2","Helen's", "Leek", 30, 10, List.of());
        Restaurant trisha = new Restaurant("3", "Trisha's", "Sevilla", 12, 8, List.of());
        Restaurant mala = new Restaurant("4", "Mala's", "New Delhi", 25, 0, List.of());

        restaurantRepository.saveAll(List.of(dalia, helen, trisha, mala));
    }

    // TODO: get all restaurants
    // TODO: get a restaurant by ID
    // TODO: update a restaurant's details
    // TODO: delete a restaurant
    // TODO: create a restaurant

    // Restaurant Admins:
    // {id: admin_id,
    //  name: bob
    //  password:
    //  email: ,
    //  restaurant_id: int}
}