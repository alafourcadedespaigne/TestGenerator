package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Answer;
import model.Question;
import utils.Utils;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class QuestionController implements Initializable {

    @FXML
    private Button btnAddQuestion;

    @FXML
    private Button btnCloseButton;

    @FXML
    private TextField txtNameAnswer;

    @FXML
    private TableView<Answer> tvAnswer;


    @FXML
    private TextField txtNameQuestion;


    @FXML
    public TableColumn<Answer, String> name;


    private TestController testController;


    private ObservableList<Answer> answerObservableList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        name.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        tvAnswer.setItems(answerObservableList);
    }

    // TODO Arreglar esto
    @FXML
    private void handleButtonClicks(javafx.event.ActionEvent mouseEvent) {
        if (mouseEvent.getSource() == btnAddQuestion) {
            Utils.loadStage(QuestionController.class, "/view/fxml/Question.fxml", "/view/icons/icon.png");
        }
    }

    @FXML
    private void closeButtonClicks() {
        // get a handle to the stage
        Stage stage = (Stage) btnCloseButton.getScene().getWindow();
        // do what you have to do
        this.answerObservableList.clear();
        stage.close();
    }

    @FXML
    private void addAnswerToQuestion() {

        if (txtNameAnswer.getText() != null && !txtNameAnswer.getText().trim().isEmpty()) {
            Answer answer = new Answer(txtNameAnswer.getText());
            if (!answerObservableList.contains(answer)) {
                answerObservableList.add(answer);
                txtNameAnswer.clear();
            }
        }
        else {
            Utils.showDialog(Alert.AlertType.ERROR, "Escriba una respuesta por favor.");
        }


    }

    @FXML
    private void addQuestionToTest() {

        if (txtNameQuestion.getText() == null && txtNameQuestion.getText().trim().isEmpty()) {

            Utils.showDialog(Alert.AlertType.ERROR, "El nombre de la prueba es obligatorio.");

        } else if (answerObservableList.isEmpty()) {
            Utils.showDialog(Alert.AlertType.WARNING, "La pregunta tiene que tener al menos una respuesta.");
        } else {
            Question question = new Question(txtNameQuestion.getText(), answerObservableList.size(),
                     new ArrayList<>(answerObservableList));
            testController.receiveDataFromQuestion(question);
            closeButtonClicks();
        }

    }

    private void clearData() {
        answerObservableList.clear();
        txtNameAnswer.clear();

    }


    public void setTestController(TestController testController) {
        this.testController = testController;
    }
}
