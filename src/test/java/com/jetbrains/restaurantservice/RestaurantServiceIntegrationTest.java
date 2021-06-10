package com.jetbrains.restaurantservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.time.DayOfWeek.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class RestaurantServiceIntegrationTest {
    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.5");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

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

        mockMvc.perform(get("/restaurants"))
               .andExpect(status().isOk())
               .andExpect(content().json("""
                                                 [{
                                                          "id" : "1",
                                                          "name" : "Dalia's",
                                                          "address" : "Rochester",
                                                          "capacity" : 20,
                                                          "openingDays" : [ "MONDAY" ]
                                                  },
                                                  {
                                                          "id" : "2",
                                                          "name" : "Helen's",
                                                          "address" : "Leek",
                                                          "capacity" : 30,
                                                          "openingDays" : [ "SUNDAY","SATURDAY" ]
                                                  },
                                                  {
                                                          "id" : "3",
                                                          "name" : "Trisha's",
                                                          "address" : "Sevilla",
                                                          "capacity" : 12,
                                                          "openingDays" : [ "TUESDAY","WEDNESDAY","THURSDAY" ]
                                                  },
                                                  {
                                                          "id" : "4",
                                                          "name" : "Mala's",
                                                          "address" : "New Delhi",
                                                          "capacity" : 25,
                                                          "openingDays" : [ "TUESDAY", "THURSDAY" ]
                                                  }
                                                  ]
                                                 """));
    }


    @Test
    void shouldGetARestaurantById() throws Exception {
        insertFourRestaurants();

        mockMvc.perform(get("/restaurants/1"))
               .andExpect(status().isOk())
               .andExpect(content().json("""
                                         {"id" : "1",
                                          "name" : "Dalia's",
                                          "address" : "Rochester",
                                          "capacity" : 20,
                                          "openingDays" : [ "MONDAY" ]
                                         }
                                         """));
    }

    @Test
    @DisplayName("Should return a 404 when the restaurant is not there")
    void shouldReturnA404WhenTheRestaurantIsNotThere() throws Exception {
        mockMvc.perform(get("/restaurants/785697865"))
               .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should delete a restaurant by ID")
    void shouldDeleteARestaurantById() throws Exception {
        // given
        insertFourRestaurants();
        String restaurantId = "3";
        // check the restaurant really is there
        mockMvc.perform(get("/restaurants/" + restaurantId))
               .andExpect(status().isOk());

        // when
        mockMvc.perform(delete("/restaurants/" + restaurantId))
               .andExpect(status().isOk());

        // then
        mockMvc.perform(get("/restaurants/" + restaurantId))
               .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should give a 404 if deleting a restaurant that is not there")
    void shouldGiveA404IfDeletingARestaurantThatIsNotThere() throws Exception {
        // given
        String restaurantId = "5647856473";

        // when
        mockMvc.perform(delete("/restaurants/" + restaurantId))
               .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should create a new restaurant")
    void shouldCreateANewRestaurant() throws Exception {
        String restaurantId = "10";
        Restaurant newRestaurant = new Restaurant(restaurantId, "New Restaurant", "London", 7, Set.of());

        // when
        mockMvc.perform(post("/restaurants")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(newRestaurant)))
               .andExpect(status().isOk());

        // then
        Optional<Restaurant> actualRestaurant = restaurantRepository.findById(restaurantId);
        assertTrue(actualRestaurant.isPresent());
        assertEquals(newRestaurant, actualRestaurant.get());
    }

    @Test
    @DisplayName("Should be able to edit a restaurant details")
    void shouldBeAbleToEditARestaurantDetails() throws Exception {
        // given
        insertFourRestaurants();

        Restaurant updatedDetails = new Restaurant("2", "Helen's Place", "Leeky kitchen", 33, Set.of());

        // when
        mockMvc.perform(put("/restaurants/2")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(updatedDetails)))
               .andExpect(status().isOk());

        // then
        Optional<Restaurant> actualRestaurant = restaurantRepository.findById("2");
        assertTrue(actualRestaurant.isPresent());
        assertEquals(updatedDetails, actualRestaurant.get());
    }

    @Test
    @DisplayName("Should return a 404 if trying to update a restaurant that does not exist")
    void shouldReturnA404IfTryingToUpdateARestaurantThatDoesNotExist() throws Exception {
        Restaurant updatedDetails = new Restaurant("657486547", "Helen's Place", "Leeky kitchen", 33, Set.of());

        // expect
        mockMvc.perform(put("/restaurants/657486547")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(updatedDetails)))
               .andExpect(status().isNotFound());
    }

    private void insertFourRestaurants() {
        restaurantRepository.deleteAll();

        Restaurant dalia = new Restaurant("1", "Dalia's", "Rochester", 20, Set.of(MONDAY));
        Restaurant helen = new Restaurant("2", "Helen's", "Leek", 30, Set.of(SATURDAY, SUNDAY));
        Restaurant trisha = new Restaurant("3", "Trisha's", "Sevilla", 12, Set.of(TUESDAY, WEDNESDAY, THURSDAY));
        Restaurant mala = new Restaurant("4", "Mala's", "New Delhi", 25, Set.of(TUESDAY, THURSDAY));

        restaurantRepository.saveAll(List.of(dalia, helen, trisha, mala));
    }

}