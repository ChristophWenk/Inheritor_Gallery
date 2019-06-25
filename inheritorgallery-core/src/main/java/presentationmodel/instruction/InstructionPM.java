package presentationmodel.instruction;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class InstructionPM {
    private final StringProperty instructionText = new SimpleStringProperty();

    public InstructionPM(String instructionText) {
        setInstructionText(instructionText);
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
