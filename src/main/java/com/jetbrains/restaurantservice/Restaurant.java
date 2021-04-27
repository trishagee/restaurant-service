package com.jetbrains.restaurantservice;

import org.springframework.data.annotation.Id;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Restaurant {
    @Id
    public String id;

    public String name;
    public String address;
    public int capacity;
    public Set<DayOfWeek> openingDays;

    public Restaurant() {
    }

    public Restaurant(String name, String address, int capacity, Set<DayOfWeek> openingDays) {
        this(null, name, address, capacity, openingDays);
    }

    public Restaurant(String id, String name, String address, int capacity, Set<DayOfWeek> openingDays) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.capacity = capacity;
        this.openingDays = openingDays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Restaurant that = (Restaurant) o;

        if (!Objects.equals(id, that.id)) return false;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
