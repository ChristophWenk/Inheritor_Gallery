package presentationmodel.uml;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FieldPM {
    private final StringProperty modifier = new SimpleStringProperty();
    private final StringProperty type = new SimpleStringProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty value = new SimpleStringProperty();

    public FieldPM(String modifier, String type, String name, String value) {
        setModifier(modifier);
        setType(type);
        setName(name);
        setValue(value);
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
