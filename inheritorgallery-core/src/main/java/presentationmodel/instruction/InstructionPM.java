package presentationmodel.instruction;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.instruction.AsciiDocService;

import java.nio.file.Path;

public class InstructionPM {
    AsciiDocService asciiDocService;
    private static Logger logger = LoggerFactory.getLogger(InstructionPM.class);


    private final StringProperty instructionText = new SimpleStringProperty();

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
