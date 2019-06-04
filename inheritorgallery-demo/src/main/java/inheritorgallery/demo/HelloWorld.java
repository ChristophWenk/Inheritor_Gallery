package inheritorgallery.demo;

import inheritorgallery.core.SomeClass;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * @author Dieter Holz
 */
public class HelloWorld extends Application {

    @Override
    public void start(Stage primaryStage) {
        SomeClass someClass = new SomeClass();
        Button    button    = new Button(someClass.getGreeting());

        StackPane rootPane = new StackPane();
        rootPane.getChildren().add(button);

        Scene myScene = new Scene(rootPane);

        primaryStage.setTitle("JavaFX App");
        primaryStage.setScene(myScene);
        primaryStage.setWidth(400);
        primaryStage.setHeight(300);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
