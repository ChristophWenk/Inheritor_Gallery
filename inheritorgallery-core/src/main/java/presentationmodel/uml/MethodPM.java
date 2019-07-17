package presentationmodel.uml;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class MethodPM {
    private final SimpleBooleanProperty lastExecuted = new SimpleBooleanProperty(false);
    private final StringProperty modifier = new SimpleStringProperty();
    private final StringProperty returnType = new SimpleStringProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final ObservableList<String> inputParameters = FXCollections.observableArrayList();
    private final SimpleListProperty<String> inputParametersProperty = new SimpleListProperty<String>(inputParameters);
    private final StringProperty implementedInClass = new SimpleStringProperty();


    public MethodPM(String modifier, String returnType, String name, List<String> inputParameters ) {
        setModifier(modifier);
        setReturnType(returnType);
        setName(name);
        this.inputParameters.addAll(inputParameters);
    }

    public boolean equals(MethodPM m){
        return  this.getName().equals(m.getName()) &&
                this.getInputParameters().equals(m.getInputParameters());
    }

    public boolean getLastExecuted() {
        return lastExecuted.get();
    }

    public SimpleBooleanProperty lastExecutedProperty() {
        return lastExecuted;
    }

    public void setLastExecuted(boolean lastExecuted) {
        this.lastExecuted.set(lastExecuted);
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

    public String getReturnType() {
        return returnType.get();
    }

    public StringProperty returnTypeProperty() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType.set(returnType);
    }

    public ObservableList<String> getInputParameters() {
        return inputParameters;
    }

    public SimpleListProperty<String> inputParametersProperty() {return inputParametersProperty;}

    public String getImplementedInClass() {
        return implementedInClass.get();
    }

    public StringProperty implementedInClassProperty() {
        return implementedInClass;
    }

    public void setImplementedInClass(String implementedInClass) {
        this.implementedInClass.set(implementedInClass);
    }
}
