package inheritorgallery.view;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class DirectoryChooserPane extends VBox implements ViewMixin {
    private DirectoryChooser directoryChooser;
    private Button button;
    private Stage primaryStage;

    public DirectoryChooserPane(Stage primaryStage, DirectoryChooser directoryChooser){
        this.primaryStage = primaryStage;
        init();

    }

    @Override
    public void initializeControls() {
        directoryChooser = new DirectoryChooser();
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
            File selectedDirectory = directoryChooser.showDialog(primaryStage);

            //selectedDirectory.getAbsolutePath();
        });

    }
}
