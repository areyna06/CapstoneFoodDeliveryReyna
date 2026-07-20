package com.projectreyna;

import java.util.List;

/**
 * Dependency Inversion Principle (DIP):
 * Controllers depend on this abstraction instead of a concrete
 * database class.
 */
public interface RestaurantRepository {

    /** Returns all restaurants, or an empty list if none exist. */
    List<Restaurant> findAll();
}
