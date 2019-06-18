package presentationmodel.uml;

import com.github.javaparser.ast.expr.SimpleName;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import service.ClassDTO;
import service.UmlService;

public class UmlPM {

    private final ObservableList<ClassPM> classes = FXCollections.observableArrayList();

    public UmlPM(UmlService service) {
        for(ClassDTO c : service.getClasses()){
            classes.add(new ClassPM(
                    c.getName(),
                    c.getFields(),
                    c.getConstructors(),
                    c.getMethods()));
        }

        //classes.add(new ClassPM(name));
    }

    public ObservableList<ClassPM> getClasses() {
        return classes;
    }
}
