import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view/fxml/Menu.fxml"));
        primaryStage.setTitle("Generador de Exámenes");
        primaryStage.getIcons().add(new Image("view/icons/icon.png"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();


    }


    public static void main(String[] args) {

        List<String> list = Arrays.asList("A", "B", "C", "D", "1", "2", "3");

        //before shuffle
        System.out.println("Antes " + list);

        // again, same insert order
        System.out.println("El mismo orden " + list);

        // shuffle or randomize
        Collections.shuffle(list);
        System.out.println("Random 1 " + list);
        System.out.println("Random 2 " + list);

        // shuffle again, different result
        Collections.shuffle(list);
        System.out.println("Otro random 1 " + list);

        launch(args);



    }
}
