package inheritorgallery.view;

import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InstructionPane extends GridPane implements ViewMixin{

    private static Logger logger = LoggerFactory.getLogger(InstructionPane.class);

    private TextArea instructionTextArea;

    public InstructionPane() {
        init();
        logger.info("Finished initializing InstructionPane");
    }

    @Override
    public void initializeControls() {
        instructionTextArea = new TextArea();
    }

    @Override
    public void layoutControls() {
        // Set IDs
        instructionTextArea.setId("instructionTextArea");

        // Add controls
        add(instructionTextArea,1,1,1,1);
    }
}
