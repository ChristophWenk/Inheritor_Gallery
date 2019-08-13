package inheritorgallery.view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentationmodel.FileChooserPM;

import java.io.File;

/**
 * View that displays all buttons needed to load classes and instructions as well as to reset the application
 */
public class FileChooserPane extends HBox implements ViewMixin {
    private static Logger logger = LoggerFactory.getLogger(FileChooserPane.class);
    private Button loadClassButton, refreshButton, loadInstructionButton;
    private Stage primaryStage;
    private FileChooserPM fileChooserPM;

    public FileChooserPane(Stage primaryStage, FileChooserPM fileChooserPM){
        this.fileChooserPM = fileChooserPM;
        this.primaryStage = primaryStage;
        init();

    }

    @Override
    public void initializeControls() {
        //directoryChooser.setInitialDirectory(new File("input"));
        loadClassButton = new Button("Load Classes");
        loadClassButton.setPrefWidth(130);
        loadInstructionButton = new Button("Load Instruction");
        loadInstructionButton.setPrefWidth(140);

        refreshButton = new Button("Reset state");
        refreshButton.setPrefWidth(120);
    }

    @Override
    public void layoutControls() {
        getChildren().addAll(loadClassButton,loadInstructionButton,refreshButton);
        setPadding(new Insets(0,2,3,2));
        setSpacing(5);

    }

    @Override
    public void setupEventHandlers() {
        loadClassButton.setOnAction(e -> {
            File selectedFile = fileChooserPM.getFileChooser().showOpenDialog(primaryStage);
            fileChooserPM.setPathForClasses(selectedFile.toPath());
        });
        loadInstructionButton.setOnAction(e -> {
            File selectedFile = fileChooserPM.getFileChooser().showOpenDialog(primaryStage);
            fileChooserPM.setPathSimpleObjectProperty(selectedFile.toPath());
        });

        refreshButton.setOnAction(e -> {
            fileChooserPM.reset();
        });


    }

}
