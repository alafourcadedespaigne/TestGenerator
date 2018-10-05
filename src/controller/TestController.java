package controller;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Answer;
import model.Question;
import model.Test;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class TestController implements Initializable {

    @FXML
    private Button btnAddQuestion;

    @FXML
    private TableView<Question> tvQuestion;

    @FXML
    public TableColumn<Question, String> nameColumn;

    @FXML
    public TableColumn<Question, Integer> countColumn;

    @FXML
    public Button btnSaveTest;

    @FXML
    public TextField testCount;

    private static ObservableList<Question> questionObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        countColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCount()).asObject());
    }

    // TODO Arreglar esto
    @FXML
    private void loadQuestion(javafx.event.ActionEvent mouseEvent) {
        if (mouseEvent.getSource() == btnAddQuestion) {
            loadStage("/view/fxml/Question.fxml");
        }
    }


    private void loadStage(String fxml) {
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(TestController.class.getResource(fxml));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image("view/icons/icon.png"));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
            QuestionController questionController = loader.getController();
            questionController.setTestController(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveDataFromQuestion(Question question) {

        questionObservableList.add(question);
        tvQuestion.setItems(questionObservableList);

    }


    @FXML
    private List<Test> createTest() {

        List<Test> tests = new ArrayList<>();
        List<Question> aux = new ArrayList<>(questionObservableList);

        for (int i = 0; i < Integer.valueOf(testCount.getText()); i++) {

            Test test = new Test();
            questionObservableList.clear();

            for (int j = 0; j < aux.size(); j++) {

                Question question = randomAnswer(aux.get(j));
                questionObservableList.add(question);
                test.setQuestions(questionObservableList);
            }

            List<Question> original = test.getQuestions();
            Collections.shuffle(original);
            test.setQuestions(original);
            tests.add(test);


        }
        return tests;
    }


    private Question randomAnswer(Question question) {

        List<Answer> originalList = question.getAnswers();
        Collections.shuffle(originalList);
        question.setAnswers(originalList);
        return question;

    }
}
