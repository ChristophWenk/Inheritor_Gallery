package inheritorgallery.view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentationmodel.FileChooserPM;

import java.io.File;

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
        loadClassButton.setPrefWidth(170);
        loadInstructionButton = new Button("Load Instruction");
        loadInstructionButton.setPrefWidth(170);

        refreshButton = new Button();
        refreshButton.setPrefWidth(50);
        refreshButton.setGraphic(
            new ImageView(
                    new Image("icons/refresh.png", 0, 17, true, false)
            ));
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
            fileChooserPM.setPathAsString(selectedFile.toURI().toString().replace("%20"," "));
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
