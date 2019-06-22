package presentationmodel.uml;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import service.ClassDTO;
import service.EdgeDTO;
import service.UmlService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class UmlPM {

    private static Logger logger = Logger.getLogger(UmlService.class.getName());

    private final ObservableList<ClassPM> classes = FXCollections.observableArrayList();
    private final ObservableList<EdgePM> edges = FXCollections.observableArrayList();
    private final ObservableMap<ClassPM,Integer> inheritanceLevel = FXCollections.observableHashMap();

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
            edges.add(new EdgePM(e.getSource(),e.getTarget(),e.getType()));
        }

        //for(EdgeDTO e : service.getEdgeDTOs()){

        //    getClassByName(e.getSource());
        //}

    }

    private ClassPM getClassByName(String s){
        Optional<ClassPM> targetClass =
                classes.stream().filter(c -> c.getName().equals(s)).findFirst();
        return targetClass.orElse(null);

    }

    private void setClassInheritanceLevel(ClassPM classNode){

//            logger.info(classNode.getName());
//            for(EdgePM edge : classNode.getEdges()){
//                Optional<ClassPM> targetClass =
//                        classes.stream().filter(c -> c.getName().equals(edge.getTarget())).findAny();
//                //logger.info(classNode.getName());
//                //recursive call if class if edge pointing to class
//                //targetClass.ifPresent(this::setClassInheritanceLevel);
//            }
    }

    public ObservableList<ClassPM> getClasses() {
        return classes;
    }

    public ObservableList<EdgePM> getEdges() {
        return edges;
    }
}
