package com.projectreyna;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Single Responsibility Principle (SRP):
 * This class has exactly one job, managing the user's session file.
 * It creates session.dat on login, reads it to validate the session
 * while navigating the system, and deletes it on logout.
 */
public class SessionManager {

    private static final String SESSION_FILE = "session.dat";

    /** Called after a successful login. Serializes the user into session.dat. */
    public static void createSession(SessionData user) {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(SESSION_FILE))) {
            out.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Deserializes session.dat. Returns null if no valid session exists. */
    public static SessionData getSession() {
        File file = new File(SESSION_FILE);
        if (!file.exists()) {
            return null;
        }
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(file))) {
            return (SessionData) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    /** Used by controllers to validate the session while navigating. */
    public static boolean isLoggedIn() {
        return getSession() != null;
    }

    /** Called on logout. Deletes session.dat automatically. */
    public static void destroySession() {
        File file = new File(SESSION_FILE);
        if (file.exists()) {
            file.delete();
        }
    }
}
