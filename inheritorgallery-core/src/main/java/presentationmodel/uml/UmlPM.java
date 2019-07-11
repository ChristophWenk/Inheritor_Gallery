package presentationmodel.uml;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.uml.ClassDTO;
import service.uml.EdgeDTO;
import service.uml.UmlService;

import java.util.*;
import java.util.stream.Collectors;

public class UmlPM {

    private static Logger logger = LoggerFactory.getLogger(UmlPM.class);

    //ToDo Observable never used, overkill?
    private final ObservableList<ClassPM> classes = FXCollections.observableArrayList();
    private final ObservableList<EdgePM> edges = FXCollections.observableArrayList();
    private final IntegerProperty inheritanceDeepness = new SimpleIntegerProperty();
    private UmlService umlService;

    public UmlPM() {
        umlService = new UmlService();
        for(ClassDTO c : umlService.getClassDTOs()){
            classes.add(new ClassPM(
                    c.isInterface(),
                    c.getFullClassName(),
                    c.getSimpleClassName(),
                    c.getSuperClassName(),
                    c.getImplementedInterfaces(),
                    c.getFields(),
                    c.getConstructors(),
                    c.getMethods()
            ));
        }
        for(EdgeDTO e : umlService.getEdgeDTOs()){
            edges.add(new EdgePM(e.getSource(),e.getTarget(),e.getType()));
        }

        setClassInheritanceLevelToHashMap(classes);
    }


    public ClassPM getClassByName(String s){
        Optional<ClassPM> targetClass =
                classes.stream().filter(c -> c.getName().equals(s)).findFirst();
        return targetClass.orElse(null);
    }

    private void setClassInheritanceLevelToHashMap(List<ClassPM> classes){
        List<ClassPM> classesLevelNotSetYet = new ArrayList<>(classes);

        int i = 1;

        while(!classesLevelNotSetYet.isEmpty()){
            i--;

            List<String> classesExtended = classesLevelNotSetYet.stream()
                    .filter(e -> e.getSuperClassName() != null && !e.getSuperClassName().equals("java.lang.Object"))
                    .map(e -> e.getSuperClassName())
                    .distinct()
                    .collect(Collectors.toList());

            List<String> classesImplemented = classesLevelNotSetYet.stream()
                    .flatMap(e -> e.getImplementedInterfaces().stream())
                    .collect(Collectors.toList());

            List<String> classesExtendedOrImplemented = new ArrayList<>(classesExtended);
            classesExtendedOrImplemented.addAll(classesImplemented);

            List<ClassPM>  classesNeverInherited = classesLevelNotSetYet.stream()
                    .filter(c -> !classesExtendedOrImplemented.contains(c.getFullClassName()))
                    .collect(Collectors.toList());

            for(ClassPM c : classesNeverInherited){
                c.setInheritanceLevel(i);
                classesLevelNotSetYet.remove(c);
            }

        }

        setInheritanceDeepness(i * -1);
        classes.forEach(c -> c.setInheritanceLevel(c.getInheritanceLevel()+getInheritanceDeepness()));
    }

    public ObservableList<ClassPM> getClasses() {
        return classes;
    }

    public ObservableList<EdgePM> getEdges() {
        return edges;
    }

    public int getInheritanceDeepness() {
        return inheritanceDeepness.get();
    }

    public IntegerProperty inheritanceDeepnessProperty() {
        return inheritanceDeepness;
    }

    public void setInheritanceDeepness(int inheritanceDeepness) {
        this.inheritanceDeepness.set(inheritanceDeepness);
    }
}
