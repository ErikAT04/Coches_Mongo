package com.erikat.cochesmongodb.Utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class AlertUtils {
    public static Optional<ButtonType> showAlert(Alert.AlertType alertType, String content, String title){
        Alert alert = new Alert(alertType);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.setTitle(title);
        return alert.showAndWait();
    }
}
