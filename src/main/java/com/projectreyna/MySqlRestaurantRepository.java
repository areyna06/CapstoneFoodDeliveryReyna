package com.projectreyna;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Single Responsibility Principle (SRP):
 * All database access for restaurants lives here, separated
 * from the UI logic in the controllers.
 */
public class MySqlRestaurantRepository implements RestaurantRepository {

    @Override
    public List<Restaurant> findAll() {
        List<Restaurant> restaurants = new ArrayList<>();

        String sql = "SELECT id, name, location FROM restaurants ORDER BY name";

        Connection connection = DBConnection.getInstance().getConnection();   // NOT in try()

        try (
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                restaurants.add(new Restaurant(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("location")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return restaurants;
    }
}
