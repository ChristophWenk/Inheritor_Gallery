package inheritorgallery.view;

import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentationmodel.instance.InstanceStatePM;
import presentationmodel.instruction.InstructionPM;

public class LeftPane extends VBox implements ViewMixin {

    private static Logger logger = LoggerFactory.getLogger(LeftPane.class);

    private InstanceStatePM instanceStatePM;
    private InstructionPM instructionPM;

    private InstructionPane instructionPane;
    private JShellPane jShellPane;

    public LeftPane(InstanceStatePM instanceStatePM, InstructionPM instructionPM) {
        this.instanceStatePM = instanceStatePM;
        this.instructionPM = instructionPM;
        init();
        logger.info("Finished initializing LeftPane");
    }

    @Override
    public void initializeControls() {
        instructionPane  = new InstructionPane(instructionPM);
        jShellPane = new JShellPane(instanceStatePM);
    }

    @Override
    public void layoutControls() {
        this.getChildren().addAll(instructionPane, jShellPane);
    }
}
