package com.jetbrains.restaurantservice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class RestaurantNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(RestaurantNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String restaurantNotFoundHandler(RestaurantNotFoundException ex) {
        return ex.getMessage();
    }
}