package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.Utils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML
    private Button btnCreateTest;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    // TODO Arreglar esto
    @FXML
    private void handleButtonClicks(javafx.event.ActionEvent mouseEvent) {
        if (mouseEvent.getSource() == btnCreateTest) {
            Utils.loadStage(MenuController.class, "/view/fxml/Test.fxml", "/view/icons/icon.png");
        }
    }

}
