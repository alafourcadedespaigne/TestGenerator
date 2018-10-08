package controller;

import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
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
import model.Aux;
import model.Question;
import model.Test;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import utils.PDF;

import java.awt.*;
import java.io.File;
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

    private Question question;

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
        this.question = question;
        questionObservableList.add(question);
        tvQuestion.setItems(questionObservableList);
    }


    @FXML
    private List<Test> createTest() {

        List<Test> tests = new ArrayList<>();
        List<Question> allQuestion = new ArrayList<>(questionObservableList);

        for (int i = 0; i < Integer.valueOf(testCount.getText()); i++) {

            Test test = new Test();
            List<Question> aux = new ArrayList<>();
            for (int j = 0; j < allQuestion.size(); j++) {
                Question q = new Question(allQuestion.get(j).getName(), allQuestion.get(j).getCount(),
                        new ArrayList<>(allQuestion.get(j).getAnswers()));
                randomAnswer(q);
                aux.add(q);

            }
            test.setQuestions(aux);
            // Cambiar el orden de las preguntas
            List<Question> original = test.getQuestions();
            Collections.shuffle(original);
            test.setQuestions(original);
            tests.add(test);


        }

        Collections.shuffle(tests);

        int num = 0;
        for (Test test : tests) {
            num++;
            int numQuestion = 1;
            List<Aux> auxList = new ArrayList<>();
            auxList.add(new Aux(test.getName()));
            for (Question question : test.getQuestions()) {
                auxList.add(new Aux(numQuestion++ + ".-" + question.getName()));
                int numAnwser = 0;
                String letters = "abcdefghijklmnopqrstuvwxyz";

                for (Answer answer : question.getAnswers()) {
                    auxList.add(new Aux("    "+letters.charAt(numAnwser++)+") " + answer.getName()));
                }
                auxList.add(new Aux(" "));
            }
            List<AbstractColumn> abstractColumns = new ArrayList<>();

            abstractColumns.add(ColumnBuilder.getNew().setColumnProperty("text", String.class.getName()).build());
            try {
                JasperPrint jasperPrint = PDF.generateDynamicListReportForJRXML(auxList, abstractColumns, null);
                String outputFilename = System.getProperty("user.home") + "/examenes/prueba"+ num + ".pdf";
                File file = new File(System.getProperty("user.home") + "/examenes");
                if (!file.exists()){
                    file.mkdirs();
                }

                JasperExportManager.exportReportToPdfFile(jasperPrint, outputFilename);
                try {
                    Desktop.getDesktop().open(new File(System.getProperty("user.home") + "/examenes"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (JRException e) {
                e.printStackTrace();
            }

        }


        return tests;
    }


    private void randomAnswer(Question question) {

        List<Answer> originalList = new ArrayList<>(question.getAnswers());
        Collections.shuffle(originalList);
        question.setAnswers(originalList);

    }
}
