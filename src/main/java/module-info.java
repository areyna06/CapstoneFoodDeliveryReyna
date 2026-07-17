module com.example.projectreyna {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;

    opens com.projectreyna to javafx.fxml;
    exports com.projectreyna;
}