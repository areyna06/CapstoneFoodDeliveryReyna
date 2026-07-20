# Capstone Food Delivery System

A JavaFX-based food delivery application where users can register, log in as a Customer or Rider, browse restaurants, add menu items to a cart, place and track orders, and rate completed deliveries. The system uses a MySQL database (via XAMPP) for data storage and Java Serialization for user session management.

## Major Features

- User registration and login with database validation (Customer and Rider roles)
- Browse restaurants and add menu items to a cart
- Checkout with multiple payment methods (Cash on Delivery, GCash, Credit Card)
- Order tracking and rider delivery view
- Order rating with stars and comments
- Persistent user sessions using Java Serialization
- Logout with automatic session file deletion

## Serialization Mechanism (Session Management)

The system implements user session management through **Java Serialization**:

1. **Session Creation** – Upon a successful login, `LoginController` calls `SessionManager.createSession()`, which serializes a `SessionData` object (the logged-in user's id, name, email, phone, address, and role) into a file named `session.dat` using `ObjectOutputStream`.
2. **Session Validation** – While navigating the system, screens validate the session by deserializing `session.dat` through `SessionManager.getSession()` / `SessionManager.isLoggedIn()` using `ObjectInputStream`. For example, `RiderController` redirects to the login screen if no valid session file exists, and the logged-in user's information (such as the welcome message) is read from the deserialized `SessionData` object.
3. **Session Deletion** – Upon logout, `LoginController.clearLoginSession()` calls `SessionManager.destroySession()`, which automatically deletes `session.dat`, and the user is redirected back to the login screen.

The `SessionData` class implements the `Serializable` interface with a declared `serialVersionUID` to ensure version compatibility during deserialization.

## SOLID Principles Applied

### 1. Single Responsibility Principle (SRP)

**Classes involved:** `SessionManager`, `MySqlUserRepository`, `LoginController`

Each class has exactly one responsibility:

- `SessionManager` handles only session file creation, validation, and deletion.
- `MySqlUserRepository` handles only database access (the SQL query for authenticating users). This code was previously inside `LoginController` and was moved out to separate database access from UI logic.
- `LoginController` and the other controllers handle only UI logic and user interaction.

**Benefit:** Database code, session logic, and UI logic are fully separated. A change to the database query never affects session handling or the UI code, making the system easier to maintain, debug, and extend.

### 2. Dependency Inversion Principle (DIP)

**Classes involved:** `UserRepository` (interface), `MySqlUserRepository` (implementation), `LoginController`

`LoginController` does not depend directly on the concrete MySQL class. It depends on the `UserRepository` interface, and `MySqlUserRepository` implements that interface:

```
LoginController  →  UserRepository (interface)  ←  MySqlUserRepository (implementation)
```

**Benefit:** The high-level module (the controller) is decoupled from the low-level module (the database code). The data source can be swapped, for example to a file-based store or a mock repository for testing, without changing any controller code, which reduces class dependencies and improves testability.

## Technologies Used

- Java / JavaFX (FXML) for the GUI
- MySQL via XAMPP
- Java Serialization (`ObjectOutputStream` / `ObjectInputStream`)
- Maven
