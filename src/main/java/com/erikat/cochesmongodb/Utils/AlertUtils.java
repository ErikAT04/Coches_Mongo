package com.erikat.cochesmongodb.Utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

import java.util.Optional;

//Gestor de alertas

public class AlertUtils {
    public static Optional<ButtonType> showAlert(Alert.AlertType alertType, String content, String title){
        Alert alert = new Alert(alertType);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.setTitle(title);
        DialogPane dpane = alert.getDialogPane(); //Coge el panel del alert para darle un estilo
        dpane.getStylesheets().add(R.getResource("src/styles/mainAppStyle.css").toString()); //El alert recibe un estilo
        return alert.showAndWait(); //Devuelve el botón que elija el usuario del alert
    }
}
