package presentationmodel.uml;

import com.github.javaparser.ast.expr.SimpleName;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UmlPM {

    private final ObservableList<ClassPM> classes = FXCollections.observableArrayList();

    public UmlPM(String name) {
        classes.add(new ClassPM(name));
    }

    public ObservableList<ClassPM> getClasses() {
        return classes;
    }
}
