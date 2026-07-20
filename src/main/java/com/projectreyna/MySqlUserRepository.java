package com.projectreyna;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Single Responsibility Principle (SRP):
 * All database access for users lives here, separated from the
 * UI logic in the controllers.
 *
 * Dependency Inversion Principle (DIP):
 * Implements the UserRepository abstraction that the controllers
 * depend on.
 */
public class MySqlUserRepository implements UserRepository {

    @Override
    public SessionData findByCredentials(String email, String password) {
        String sql = """
                SELECT id, name, email, phone, address, role
                FROM users
                WHERE email = ? AND password = ?
                LIMIT 1
                """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, email);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return null;
                }

                return new SessionData(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("address"),
                        resultSet.getString("role")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
