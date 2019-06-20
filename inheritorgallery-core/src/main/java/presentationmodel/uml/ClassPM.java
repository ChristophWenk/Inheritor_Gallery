package presentationmodel.uml;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class ClassPM {
    private final StringProperty name = new SimpleStringProperty();
    private final ObservableList<FieldPM> fields = FXCollections.observableArrayList();
    private final ObservableList<ConstuctorPM> constructors = FXCollections.observableArrayList();
    private final ObservableList<MethodPM> methods = FXCollections.observableArrayList();



    public ClassPM(String name, List<String> fields, List<String> constructors, List<String> methods) {
        setName(name);
        for(String f : fields){this.fields.add((new FieldPM(f)));}
        for(String c : constructors){this.constructors.add((new ConstuctorPM(c)));}
        for(String m : methods){this.methods.add((new MethodPM(m)));}
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

    public ObservableList<FieldPM> getFields() {
        return fields;
    }

    public ObservableList<ConstuctorPM> getConstructors() {
        return constructors;
    }

    public ObservableList<MethodPM> getMethods() {
        return methods;
    }
}
