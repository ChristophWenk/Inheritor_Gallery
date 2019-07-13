package presentationmodel.uml;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class ConstructorPM {
    private final StringProperty modifier = new SimpleStringProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final ObservableList<String> inputParameters = FXCollections.observableArrayList();

    public ConstructorPM(String modifier, String name, List<String> inputParameters) {
        setModifier(modifier);
        setName(name);
        this.inputParameters.addAll(inputParameters);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getModifier() {
        return modifier.get();
    }

    public StringProperty modifierProperty() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier.set(modifier);
    }

    public ObservableList<String> getInputParameters() {
        return inputParameters;
    }
}
