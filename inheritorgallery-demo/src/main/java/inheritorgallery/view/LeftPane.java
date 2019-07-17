package inheritorgallery.view;

import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentationmodel.instance.InstanceStatePM;
import presentationmodel.instruction.InstructionPM;

public class LeftPane extends VBox implements ViewMixin {

    private static Logger logger = LoggerFactory.getLogger(LeftPane.class);


    private InstanceStatePM instanceStatePM;
    private InstructionPM instructionPM;
    private DirectoryChooser directoryChooser;

    private InstructionPane instructionPane;
    private JShellPane jShellPane;
    private DirectoryChooserPane directoryChooserPane;
    private Stage primaryStage;

    public LeftPane(Stage primaryStage, DirectoryChooser directoryChooser, InstanceStatePM instanceStatePM, InstructionPM instructionPM) {
        this.primaryStage = primaryStage;
        this.directoryChooser = directoryChooser;
        this.instanceStatePM = instanceStatePM;
        this.instructionPM = instructionPM;
        init();
        logger.info("Finished initializing LeftPane");
    }

    @Override
    public void initializeControls() {
        directoryChooserPane = new DirectoryChooserPane(primaryStage, directoryChooser);
        instructionPane  = new InstructionPane(instructionPM);
        jShellPane = new JShellPane(instanceStatePM);
    }

    @Override
    public void layoutControls() {
        this.getChildren().addAll(directoryChooserPane,instructionPane, jShellPane);
    }
}
