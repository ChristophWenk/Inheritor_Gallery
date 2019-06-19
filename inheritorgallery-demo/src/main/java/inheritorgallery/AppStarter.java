package inheritorgallery;

import inheritorgallery.view.ApplicationUI;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import presentationmodel.uml.UmlPM;
import service.UmlService;

/**
 * @author Christoph Wenk, Dimitri Muralt
 */
// TODO Include PM everywhere
public class AppStarter extends Application {

    @Override
    public void start(Stage primaryStage) {
    /*    SomeClass someClass = new SomeClass();
        Button    button    = new Button(someClass.getGreeting());

        StackPane rootPane = new StackPane();
        rootPane.getChildren().add(button);

        Scene myScene = new Scene(rootPane);

        primaryStage.setTitle("JavaFX App");
        primaryStage.setScene(myScene);
        primaryStage.setWidth(400);
        primaryStage.setHeight(300);
        primaryStage.show();
     */
        UmlService umlService = new UmlService();
        UmlPM pm = new UmlPM(umlService);

        Parent rootPane = new ApplicationUI(pm);

        Scene scene = new Scene(rootPane);
        String stylesheet = AppStarter.class.getResource("view/css/style.css").toExternalForm();
        scene.getStylesheets().add(stylesheet);

        //primaryStage.titleProperty().bind(pm.applicationTitleProperty());
        primaryStage.setTitle("Inheritor Gallery");
        primaryStage.setScene(scene);
        primaryStage.setWidth(600);
        primaryStage.setHeight(300);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
