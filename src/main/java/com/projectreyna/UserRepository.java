package com.projectreyna;

/**
 * Dependency Inversion Principle (DIP):
 * High-level modules (controllers) depend on this abstraction
 * instead of a concrete database class. The data source can be
 * swapped (MySQL, file, mock for testing) without touching the UI.
 */
public interface UserRepository {

    /**
     * Looks up a user by email and password.
     *
     * @return the user's session data if the credentials match,
     *         or null if no matching user was found.
     */
    SessionData findByCredentials(String email, String password);
}
