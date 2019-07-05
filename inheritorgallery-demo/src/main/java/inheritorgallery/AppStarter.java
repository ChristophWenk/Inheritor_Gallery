package inheritorgallery;

import inheritorgallery.view.ApplicationUI;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import presentationmodel.jshell.JShellPM;
import presentationmodel.uml.UmlPM;
import service.uml.UmlService;

/**
 * @author Christoph Wenk, Dimitri Muralt
 */
// TODO Include PM everywhere
public class AppStarter extends Application {

    @Override
    public void start(Stage primaryStage) {

        UmlService umlService = new UmlService();
        UmlPM umlPM = new UmlPM(umlService);

        JShellPM jShellPM = new JShellPM();

        Parent rootPane = new ApplicationUI(jShellPM, umlPM);



        Scene scene = new Scene(rootPane);

        String stylesheet = AppStarter.class.getResource("view/css/style.css").toExternalForm();
        scene.getStylesheets().add(stylesheet);

        primaryStage.setTitle("Inheritor Gallery");
        primaryStage.setScene(scene);
        primaryStage.setWidth(1280);
        primaryStage.setHeight(640);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
