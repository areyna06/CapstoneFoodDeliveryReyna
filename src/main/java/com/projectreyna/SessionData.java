package com.projectreyna;

import java.io.Serializable;

/**
 * Serializable snapshot of the logged-in user's information.
 * This object is what gets written into session.dat.
 */
public class SessionData implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int id;
    private final String name;
    private final String email;
    private final String phone;
    private final String address;
    private final String role;

    public SessionData(int id, String name, String email,
                       String phone, String address, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role = role;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getRole() { return role; }
}
