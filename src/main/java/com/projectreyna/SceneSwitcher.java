package com.projectreyna;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneSwitcher {
    /** Switches the window that 'anyNode' lives in to the given FXML screen. */
    public static void switchTo(String fxmlFile, Node anyNode) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource(fxmlFile));
            Stage stage = (Stage) anyNode.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace(); // if this prints, the fxml name/controller is wrong
        }
    }
}