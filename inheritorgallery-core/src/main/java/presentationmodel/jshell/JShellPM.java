package presentationmodel.jshell;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class JShellPM {
    private final StringProperty input = new SimpleStringProperty();
    private final StringProperty output = new SimpleStringProperty();

    public String getInput() {
        return input.get();
    }

    public StringProperty inputProperty() {
        return input;
    }

    public void setInput(String input) {
        this.input.set(input);
    }

    public String getOutput() {
        return output.get();
    }

    public StringProperty outputProperty() {
        return output;
    }

    public void setOutput(String output) {
        this.output.set(output);
    }
}
