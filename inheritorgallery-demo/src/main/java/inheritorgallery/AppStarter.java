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
public class AppStarter extends Application {

    @Override
    public void start(Stage primaryStage) {
        UmlService umlService = new UmlService();
        UmlPM pm = umlService.createUmlPM();

        Parent rootPane = new ApplicationUI(pm);

        Scene scene = new Scene(rootPane);
        String stylesheet = AppStarter.class.getResource("view/css/style.css").toExternalForm();
        scene.getStylesheets().add(stylesheet);

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
