package presentationmodel.uml;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import service.ClassDTO;
import service.EdgeDTO;
import service.UmlService;

public class UmlPM {

    private final ObservableList<ClassPM> classes = FXCollections.observableArrayList();
    private final ObservableList<EdgePM> edges = FXCollections.observableArrayList();

    public UmlPM(UmlService service) {
        for(ClassDTO c : service.getClassDTOs()){
            classes.add(new ClassPM(
                    c.getName(),
                    c.getFields(),
                    c.getConstructors(),
                    c.getMethods()
            ));
        }
        for(EdgeDTO e : service.getEdgeDTOs()){
            edges.add(new EdgePM(
                    e.getSource(),
                    e.getTarget(),
                    e.getType()
            ));
        }
    }

    public ObservableList<ClassPM> getClasses() {
        return classes;
    }

    public ObservableList<EdgePM> getEdges() {
        return edges;
    }
}
