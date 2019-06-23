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
    private int inheritanceDeepness;

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
        List<EdgePM> edgesToFilter = new ArrayList<>(edges);

        List<String> remainingTargets;

        int i = 1;
        while(!classesToFilter.isEmpty()){
            i--;
            remainingTargets = edgesToFilter.stream().map(EdgePM::getTarget).collect(Collectors.toList());

            List<String> finalAllTargets = remainingTargets;

            List<ClassPM>  classesNeverInherited =
                    classesToFilter.stream().filter(c -> !finalAllTargets.contains(c.getName())).collect(Collectors.toList());
            for(ClassPM c : classesNeverInherited){
                inheritanceLevel.put(c,i);
            }

            classesToFilter.removeAll(classesNeverInherited);

            List<String> classesRemoved = classesNeverInherited.stream().map(ClassPM::getName).collect(Collectors.toList());

            edgesToFilter =  edgesToFilter.stream()
                    .filter(e -> !classesRemoved.contains(e.getSource())).collect(Collectors.toList());
        }

        setInheritanceDeepness(i * -1);

        inheritanceLevel.forEach((classPM,level) -> inheritanceLevel.put(classPM,level+getInheritanceDeepness()));
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

    public int getInheritanceDeepness() {
        return inheritanceDeepness;
    }

    public void setInheritanceDeepness(int inheritanceDeepness) {
        this.inheritanceDeepness = inheritanceDeepness;
    }
}
