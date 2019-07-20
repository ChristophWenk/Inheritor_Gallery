package inheritorgallery.view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import presentationmodel.FileChooserPM;

import java.io.File;

public class DirectoryChooserPane extends HBox implements ViewMixin {
    private Button loadButton, refreshButton;
    private Stage primaryStage;
    private FileChooserPM fileChooserPM;

    public DirectoryChooserPane(Stage primaryStage, FileChooserPM fileChooserPM){
        this.fileChooserPM = fileChooserPM;
        this.primaryStage = primaryStage;
        init();

    }

    @Override
    public void initializeControls() {
        //directoryChooser.setInitialDirectory(new File("input"));
        loadButton = new Button("Load");
        loadButton.setPrefWidth(200);
        refreshButton = new Button("Refresh");
        refreshButton.setPrefWidth(200);
    }

    @Override
    public void layoutControls() {
        getChildren().addAll(loadButton,refreshButton);
        setPadding(new Insets(0,2,3,2));
        setSpacing(5);

    }

    @Override
    public void setupEventHandlers() {
        loadButton.setOnAction(e -> {
            File selectedDirectory = fileChooserPM.getFileChooser().showOpenDialog(primaryStage);
            fileChooserPM.setPath(selectedDirectory.toPath());
        });

        refreshButton.setOnAction(e -> {
            fileChooserPM.reset();
        });


    }

}
