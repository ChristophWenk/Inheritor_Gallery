package inheritorgallery.view;

import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentationmodel.jshell.JShellPM;

public class LeftPane extends VBox implements ViewMixin {

    private static Logger logger = LoggerFactory.getLogger(LeftPane.class);

    private JShellPM jShellPM;

    private InstructionPane instructionPane;
    private JShellPane jShellPane;

    public LeftPane(JShellPM jShellPM) {
        this.jShellPM = jShellPM;
        init();
        logger.info("Finished initializing LeftPane");
    }

    @Override
    public void initializeControls() {
        instructionPane  = new InstructionPane();
        jShellPane = new JShellPane(jShellPM);
    }

    @Override
    public void layoutControls() {
        this.getChildren().addAll(instructionPane, jShellPane);
    }
}
