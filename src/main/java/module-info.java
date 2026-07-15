module com.example.projectreyna {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.projectreyna to javafx.fxml;
    exports com.projectreyna;

    opens com.projectreyna.LoginController to javafx.fxml;
    exports com.projectreyna.LoginController;
}