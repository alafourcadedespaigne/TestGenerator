package controller;

import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Answer;
import model.Aux;
import model.Question;
import model.Test;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import utils.PDF;
import utils.Utils;

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
        addButtonToTable();

        testCount.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    testCount.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
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


    /**
     * Crear un examen con preguntas y respuestas aleatorias
     */

    @FXML
    private void createTest() {

        if (testCount.getText() != null && !testCount.getText().trim().isEmpty() && Integer.valueOf(testCount.getText()) >= 1) {


            if (!questionObservableList.isEmpty()) {
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
                            auxList.add(new Aux("    " + letters.charAt(numAnwser++) + ") " + answer.getName()));
                        }
                        auxList.add(new Aux(" "));
                    }
                    List<AbstractColumn> abstractColumns = new ArrayList<>();

                    abstractColumns.add(ColumnBuilder.getNew().setColumnProperty("text", String.class.getName()).build());
                    try {
                        JasperPrint jasperPrint = PDF.generateDynamicListReportForJRXML(auxList, abstractColumns, null);
                        String outputFilename = System.getProperty("user.home") + "/examenes/prueba" + num + ".pdf";
                        File file = new File(System.getProperty("user.home") + "/examenes");
                        if (!file.exists()) {
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
            } else {
                Utils.showDialog(Alert.AlertType.ERROR, "No se puede crear un exámen sin preguntas.");
            }

        } else {
            Utils.showDialog(Alert.AlertType.ERROR, "Seleccione la cantidad de exámenes a generar");
        }
    }

    /**
     * Generar respuestas random para una pregunta
     * @param question pregunta
     */
    private void randomAnswer(Question question) {

        List<Answer> originalList = new ArrayList<>(question.getAnswers());
        Collections.shuffle(originalList);
        question.setAnswers(originalList);

    }


    /**
     * Agregar botones de accion al componente TableView
     */
    private void addButtonToTable() {
        TableColumn<Question, Void> colBtn = new TableColumn("Acción");
        colBtn.maxWidthProperty().setValue(400);

        Callback<TableColumn<Question, Void>, TableCell<Question, Void>> cellFactory = new Callback<TableColumn<Question, Void>, TableCell<Question, Void>>() {
            @Override
            public TableCell<Question, Void> call(final TableColumn<Question, Void> param) {
                final TableCell<Question, Void> cell = new TableCell<Question, Void>() {

                    private final Button btnDelete = new Button("", new FontAwesomeIconView(FontAwesomeIcon.TRASH));

                    {
                        btnDelete.setTooltip(new Tooltip("Elminar"));
                        btnDelete.setOnAction((ActionEvent event) -> {
                            Question data = getTableView().getItems().get(getIndex());
                            System.out.println("selectedData: " + data);
                        });
                    }

                    private final Button btnUpdate = new Button("", new FontAwesomeIconView(FontAwesomeIcon.EDIT));

                    {
                        btnUpdate.setTooltip(new Tooltip("Editar"));
                        btnUpdate.setOnAction((ActionEvent event) -> {
                            Question data = getTableView().getItems().get(getIndex());
                            System.out.println("selectedData: " + data);
                        });
                    }

                    HBox pane = new HBox(btnDelete, btnUpdate);


                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(pane);
                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);

        tvQuestion.getColumns().add(colBtn);
    }

}
