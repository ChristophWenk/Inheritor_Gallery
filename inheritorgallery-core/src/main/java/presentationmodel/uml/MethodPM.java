package presentationmodel.uml;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * Presentationmodel that stores the current state of a method
 */
public class MethodPM {
    private final ObjectProperty<ClassPM> declaredInClass = new SimpleObjectProperty<>();
    private final SimpleBooleanProperty lastExecuted = new SimpleBooleanProperty(false);
    private final SimpleStringProperty lastExecutedAsString = new SimpleStringProperty("");
    private final StringProperty modifier = new SimpleStringProperty();
    private final StringProperty returnType = new SimpleStringProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final ObservableList<String> inputParameters = FXCollections.observableArrayList();
    private final SimpleListProperty<String> inputParametersProperty = new SimpleListProperty<String>(inputParameters);
    private final StringProperty implementedInClass = new SimpleStringProperty();

    /**
     * Create the MethodPM
     * @param classPM The class where the method is declared in
     * @param modifier Modifier of the method (e.g. public)
     * @param returnType Return type of the method (e.g. String)
     * @param name Name of the method
     * @param inputParameters Parameters that may be passed to the method
     */
    public MethodPM(ClassPM classPM, String modifier, String returnType, String name, List<String> inputParameters ) {
        setDeclaredInClass(classPM);
        setModifier(modifier);
        setReturnType(returnType);
        setName(name);
        this.inputParameters.addAll(inputParameters);
    }

    /**
     * Compare MethodPM to other MethodPM
     * @param m The MethodPM that should be compared
     * @return True if the other MethodPM is equal to this MethodPM
     */
    public boolean equals(MethodPM m){
        return  this.getName().equals(m.getName()) &&
                this.getInputParameters().equals(m.getInputParameters());
    }

    /**
     * Compare MethodPM to other MethodPM declared in a specific class
     * @param m The MethodPM that should be compared
     * @param classPM The class that should be compared
     * @return True if the other MethodPM is equal to this MethodPM
     */
    public boolean equals(MethodPM m, ClassPM classPM){
        return  getDeclaredInClass().getName().equals(classPM.getName()) &&
                this.getName().equals(m.getName()) &&
                this.getInputParameters().equals(m.getInputParameters());
    }

    public ClassPM getDeclaredInClass() {
        return declaredInClass.get();
    }

    public ObjectProperty<ClassPM> declaredInClassProperty() {
        return declaredInClass;
    }

    public void setDeclaredInClass(ClassPM declaredInClass) {
        this.declaredInClass.set(declaredInClass);
    }

    public boolean getLastExecuted() {
        return lastExecuted.get();
    }

    public SimpleBooleanProperty lastExecutedProperty() {
        return lastExecuted;
    }

    public void setLastExecuted(boolean lastExecuted) {
        this.lastExecuted.set(lastExecuted);
        if(getLastExecuted()) setLastExecutedAsString("-fx-background-color: rgba(255, 19, 68, 0.99);");
        else setLastExecutedAsString("");
    }

    public String getLastExecutedAsString() {
        return lastExecutedAsString.get();
    }

    public SimpleStringProperty lastExecutedAsStringProperty() {
        return lastExecutedAsString;
    }

    public void setLastExecutedAsString(String lastExecutedAsString) {
        this.lastExecutedAsString.set(lastExecutedAsString);
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
