package presentationmodel.uml;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Presentationmodel that stores the state of a field
 */
public class FieldPM {
    private final StringProperty declaringClass = new SimpleStringProperty();
    private final StringProperty modifier = new SimpleStringProperty();
    private final StringProperty type = new SimpleStringProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty value = new SimpleStringProperty();

    /**
     * Create the FieldPM
     * @param declaringClass The class that declares the field
     * @param modifier Modifier of the field (e.g. public)
     * @param type Type of the field (e.g. static)
     * @param name Name of the field
     * @param value Value of the field
     */
    public FieldPM(String declaringClass, String modifier, String type, String name, String value) {
        setDeclaringClass(declaringClass);
        setModifier(modifier);
        setType(type);
        setName(name);
        setValue(value);
    }

    public String getDeclaringClass() {
        return declaringClass.get();
    }

    public StringProperty declaringClassProperty() {
        return declaringClass;
    }

    public void setDeclaringClass(String declaringClass) {
        this.declaringClass.set(declaringClass);
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

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getValue() {
        return value.get();
    }

    public StringProperty valueProperty() {
        return value;
    }

    public void setValue(String value) {
        this.value.set(value);
    }
}
