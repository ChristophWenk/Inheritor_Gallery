package inheritorgallery;

import inheritorgallery.view.ApplicationUI;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import presentationmodel.ColorPM;
import presentationmodel.instance.InstanceStatePM;
import presentationmodel.instruction.InstructionPM;
import presentationmodel.uml.UmlPM;
import service.instruction.AsciiDocService;

/**
 * @author Christoph Wenk, Dimitri Muralt
 */
public class AppStarter extends Application {

    @Override
    public void start(Stage primaryStage) {

        AsciiDocService asciiDocService = new AsciiDocService();

        UmlPM umlPM = new UmlPM();
        InstanceStatePM instanceStatePM = new InstanceStatePM(umlPM);
        InstructionPM instructionPM = new InstructionPM(asciiDocService);
        ColorPM colorPM = new ColorPM();

        Parent rootPane = new ApplicationUI(instanceStatePM, umlPM, instructionPM, colorPM);

        Scene scene = new Scene(rootPane);

        String stylesheet = AppStarter.class.getResource("view/css/style.css").toExternalForm();
        scene.getStylesheets().add(stylesheet);

        primaryStage.setTitle("Inheritor Gallery");
        primaryStage.getIcons().add(new Image(AppStarter.class.getResourceAsStream("view/images/constructor.png")));
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
