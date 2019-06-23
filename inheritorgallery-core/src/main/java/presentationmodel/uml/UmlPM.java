package presentationmodel.uml;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import service.ClassDTO;
import service.EdgeDTO;
import service.UmlService;

import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class UmlPM {

    private static Logger logger = Logger.getLogger(UmlService.class.getName());

    //ToDo Observable never used, overkill?
    private final ObservableList<ClassPM> classes = FXCollections.observableArrayList();
    private final ObservableList<EdgePM> edges = FXCollections.observableArrayList();
    private final Map<ClassPM,Integer> inheritanceLevel = new HashMap<>();

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

        setClassInheritanceLevel(classes, edges);

    }


    public ClassPM getClassByName(String s){
        Optional<ClassPM> targetClass =
                classes.stream().filter(c -> c.getName().equals(s)).findFirst();
        return targetClass.orElse(null);

    }

    private void setClassInheritanceLevel(List<ClassPM> classes, List<EdgePM> edges){
        List<ClassPM> classesToFilter = new ArrayList<>(classes);
        List<String> allTargets = edges.stream().map(EdgePM::getTarget).collect(Collectors.toList());
        List<String> allSources = edges.stream().map(EdgePM::getSource).collect(Collectors.toList());

        List<ClassPM>  classesInheritNothing =
                classesToFilter.stream().filter(c -> !allSources.contains(c.getName())).collect(Collectors.toList());
        for(ClassPM c : classesInheritNothing){
            inheritanceLevel.put(c,0);
            logger.info("0 " + c.getName());
        }
        classesToFilter.removeAll(classesInheritNothing);

        List<ClassPM>  classesNeverInherited =
                classesToFilter.stream().filter(c -> !allTargets.contains(c.getName())).collect(Collectors.toList());
        for(ClassPM c : classesNeverInherited){
            inheritanceLevel.put(c,0);
            logger.info("1 " + c.getName());
        }
        classesToFilter.removeAll(classesNeverInherited);


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

    public Map<ClassPM, Integer> getInheritanceLevel() {
        return inheritanceLevel;
    }
}
