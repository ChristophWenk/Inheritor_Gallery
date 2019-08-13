package presentationmodel.instruction;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.instruction.AsciiDocService;

import java.nio.file.Path;

/**
 * Presentationmodel that stores the current state of the instruction text
 */
public class InstructionPM {
    AsciiDocService asciiDocService;
    private static Logger logger = LoggerFactory.getLogger(InstructionPM.class);

    private final StringProperty instructionText = new SimpleStringProperty();

    /**
     * Create the InstructionPM
     * @param asciiDocService The service needed to convert an input instruction
     */
    public InstructionPM(AsciiDocService asciiDocService) {
        this.asciiDocService = asciiDocService;
    }

    public String getInstructionText() {
        return instructionText.get();
    }

    public StringProperty instructionTextProperty() {
        return instructionText;
    }

    public void setInstructionText(Path instructionText) {
        this.instructionText.set(asciiDocService.convertFile(instructionText));
    }
}
