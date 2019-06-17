package presentationmodel.uml;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ClassPM {
    private final StringProperty name = new SimpleStringProperty();

    public ClassPM(String name) {
        setName(name);
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
}
