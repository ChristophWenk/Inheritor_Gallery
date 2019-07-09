package presentationmodel.instruction;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import service.instruction.AsciiDocService;

public class InstructionPM {
    AsciiDocService asciiDocService;

    private final StringProperty instructionText = new SimpleStringProperty();

    public InstructionPM(AsciiDocService asciiDocService) {
        this.asciiDocService = asciiDocService;
        setInstructionText(asciiDocService.convertFile("/instructions/instructions.adoc"));
    }

    public void setInstructionText(String instructionText) {
        this.instructionText.set(instructionText);
    }

    public String getInstructionText() {
        return instructionText.get();
    }

    public StringProperty instructionTextProperty() {
        return instructionText;
    }
}
