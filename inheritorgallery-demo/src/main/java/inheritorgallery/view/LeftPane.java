package inheritorgallery.view;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentationmodel.FileChooserPM;
import presentationmodel.instance.InstanceStatePM;
import presentationmodel.instruction.InstructionPM;

public class LeftPane extends VBox implements ViewMixin {

    private static Logger logger = LoggerFactory.getLogger(LeftPane.class);

    private InstanceStatePM instanceStatePM;
    private InstructionPM instructionPM;
    private FileChooserPM fileChooserPM;

    private InstructionPane instructionPane;
    private JShellPane jShellPane;
    private FileChooserPane fileChooserPane;
    private Stage primaryStage;

    public LeftPane(Stage primaryStage, FileChooserPM fileChooserPM, InstanceStatePM instanceStatePM, InstructionPM instructionPM) {
        this.primaryStage = primaryStage;
        this.fileChooserPM = fileChooserPM;
        this.instanceStatePM = instanceStatePM;
        this.instructionPM = instructionPM;
        init();
        logger.info("Finished initializing LeftPane");
    }

    @Override
    public void initializeControls() {
        fileChooserPane = new FileChooserPane(primaryStage, fileChooserPM);
        instructionPane  = new InstructionPane(instructionPM);
        jShellPane = new JShellPane(instanceStatePM);
    }

    @Override
    public void layoutControls() {
        this.getChildren().addAll(fileChooserPane,instructionPane, jShellPane);
    }
}
