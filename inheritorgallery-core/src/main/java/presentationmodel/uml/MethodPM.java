package presentationmodel.uml;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class MethodPM {
    private final StringProperty modifier = new SimpleStringProperty();
    private final StringProperty returnType = new SimpleStringProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final ObservableList<String> inputParameters = FXCollections.observableArrayList();
    private final StringProperty declaredInClass = new SimpleStringProperty();
    private final StringProperty implementedInClass = new SimpleStringProperty();


    public MethodPM(String modifier, String returnType, String name, List<String> inputParameters ) {
        setModifier(modifier);
        setReturnType(returnType);
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

    public String getDeclaredInClass() {
        return declaredInClass.get();
    }

    public StringProperty declaredInClassProperty() {
        return declaredInClass;
    }

    public void setDeclaredInClass(String declaredInClass) {
        this.declaredInClass.set(declaredInClass);
    }

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
