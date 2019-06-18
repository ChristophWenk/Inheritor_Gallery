package inheritorgallery.view;

import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InteractionPane extends VBox implements ViewMixin {

    private static Logger logger = LoggerFactory.getLogger(InteractionPane.class);

    private InstructionPane instructionPane;
    private ConsolePane consolePane;

    public InteractionPane() {
        init();
        logger.info("Finished initializing InteractionPane");
    }

    @Override
    public void initializeControls() {
        instructionPane  = new InstructionPane();
        consolePane = new ConsolePane();
    }

    @Override
    public void layoutControls() {
        this.getChildren().addAll(instructionPane,consolePane);
    }
}
