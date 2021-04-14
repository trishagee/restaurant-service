package com.jetbrains.restaurantservice;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Restaurant {
    @Id
    public String id;

    public String name;
    public String address;
    public int indoorCapacity;
    public int outdoorCapacity;
    public List<LocalDateTime> openingHours;

    public Restaurant(String name, String address, int indoorCapacity, int outdoorCapacity, List<LocalDateTime> openingHours) {
        this.name = name;
        this.address = address;
        this.indoorCapacity = indoorCapacity;
        this.outdoorCapacity = outdoorCapacity;
        this.openingHours = openingHours;
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
