package inheritorgallery.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import presentationmodel.FileChooserPM;

import java.io.File;

public class DirectoryChooserPane extends HBox implements ViewMixin {
    private Button button;
    private Stage primaryStage;
    private FileChooserPM fileChooserPM;
    private Label pathLabel;

    public DirectoryChooserPane(Stage primaryStage, FileChooserPM fileChooserPM){
        this.fileChooserPM = fileChooserPM;
        this.primaryStage = primaryStage;
        init();

    }

    @Override
    public void initializeControls() {
        //directoryChooser.setInitialDirectory(new File("input"));
        button = new Button("Select Folder");
        pathLabel = new Label();
    }

    @Override
    public void layoutControls() {
        getChildren().addAll(button, pathLabel);

    }

    @Override
    public void setupEventHandlers() {
        button.setOnAction(e -> {
            File selectedDirectory = fileChooserPM.getFileChooser().showOpenDialog(primaryStage);
            fileChooserPM.setPath(selectedDirectory.toPath());
        });
    }

    @Override
    public void setupBindings(){
        pathLabel.textProperty().bind(fileChooserPM.pathProperty().asString());
    }

}
