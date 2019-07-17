package inheritorgallery.view;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import presentationmodel.DirectoryChooserPM;

import java.io.File;

public class DirectoryChooserPane extends VBox implements ViewMixin {
    private Button button;
    private Stage primaryStage;
    private DirectoryChooserPM directoryChooserPM;

    public DirectoryChooserPane(Stage primaryStage, DirectoryChooserPM directoryChooserPM){
        this.directoryChooserPM = directoryChooserPM;
        this.primaryStage = primaryStage;
        init();

    }

    @Override
    public void initializeControls() {
        //directoryChooser.setInitialDirectory(new File("input"));
        button = new Button("Select Folder");

    }

    @Override
    public void layoutControls() {
        getChildren().addAll(button);

    }

    @Override
    public void setupEventHandlers() {
        button.setOnAction(e -> {
            File selectedDirectory = directoryChooserPM.getDirectoryChooser().showDialog(primaryStage);

            //selectedDirectory.getAbsolutePath();
        });

    }
}
