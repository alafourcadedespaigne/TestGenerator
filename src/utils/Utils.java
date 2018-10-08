package utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Utils {

    public static void loadStage(Class<?> clazz, String fxml, String imageUrl) {
        try {
            Parent root = FXMLLoader.load(clazz.getResource(fxml));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image(imageUrl));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showDialog(Alert.AlertType type, String message) {

        Alert alert = new Alert(type);
        if (type == Alert.AlertType.ERROR){
            alert.setTitle("Error");
            alert.setHeaderText("Ha ocurrido un error");
        }

        if (type == Alert.AlertType.WARNING){
            alert.setTitle("Advertencia");
            alert.setHeaderText("Mensaje de Advertencia");
        }

        if (type == Alert.AlertType.INFORMATION){
            alert.setTitle("Información");
            alert.setHeaderText("Mensaje de información");
        }

        if (type == Alert.AlertType.CONFIRMATION){
            alert.setTitle("Confirmación");
            alert.setHeaderText("Mensaje de confirmación");
        }
        alert.setContentText(message);
        alert.showAndWait();
    }
}
