package presentationmodel.instance;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentationmodel.uml.ClassPM;
import presentationmodel.uml.FieldPM;
import presentationmodel.uml.MethodPM;
import presentationmodel.uml.UmlPM;
import service.jshell.dto.FieldDTO;

import java.util.*;
import java.util.stream.Collectors;


public class ObjectPM {

    private static Logger logger = LoggerFactory.getLogger(ObjectPM.class);

    private final StringProperty objectId = new SimpleStringProperty();
    private final ObjectProperty<ClassPM> objectRootClass = new SimpleObjectProperty<>();
    private final ObjectProperty<ClassPM> objectTree = new SimpleObjectProperty<>();
    private final ObservableList<ClassPM> objectParts = FXCollections.observableArrayList();
    private final ObservableList<ReferencePM> references = FXCollections.observableArrayList();
    private final IntegerProperty objectWidth = new SimpleIntegerProperty();

    private UmlPM umlPM;

    public ObjectPM(UmlPM umlPM, String objectId, ClassPM objectRootClass, List<FieldDTO> fieldDTOs){
        this.umlPM = umlPM;
        setObjectId(objectId);
        setObjectRootClass(objectRootClass);

        setObjectStructure();

        for(ClassPM part : objectParts){
            setFieldValues(part, fieldDTOs);
        }

        updateOverridenMethods();

        generateObjectTree(objectRootClass,fieldDTOs);
    }

    private void generateObjectTree(ClassPM objectRootClass, List<FieldDTO> fieldDTOs){
        setObjectTree(objectRootClass.clone());

        ClassPM currentNode = getObjectTree();

        while (true){
            setFieldValues(currentNode,fieldDTOs);
            updateOverridenMethodsTree(currentNode);

            if(objectRootClass.hasSuperClass()){
                currentNode.setSuperClass(objectRootClass.getSuperClass().clone());
                currentNode = currentNode.getSuperClass();
                objectRootClass = objectRootClass.getSuperClass();
            }
            else break;
        }
    }

    public void setObjectStructure(){
        ClassPM rootClass = objectRootClass.get().clone();

        objectParts.add(rootClass);

        List<ClassPM> implementedInterfaces = rootClass.getImplementedInterfacesAsString().stream()
                .map(e -> umlPM.getClassByFullName(e))
                .collect(Collectors.toList());

        setObjectWidth(implementedInterfaces.size()+1);

        while(rootClass.hasSuperClass()){
            rootClass  = umlPM.getClassByFullName(rootClass.getSuperClassName()).clone();
            objectParts.add(rootClass);
        }

        if(!implementedInterfaces.isEmpty())  objectParts.addAll(implementedInterfaces);
    }

    private void setFieldValues(ClassPM part, List<FieldDTO> fieldDTOs){
        for (FieldPM field : part.getFields()){
            Optional<FieldDTO> fieldOptional =  fieldDTOs.stream()
                    .filter(e -> e.getDeclaringClass().equals(part.getFullClassName()))
                    .filter(e -> e.getName().equals(field.getName()))
                    .findFirst();
            fieldOptional.ifPresent(fieldDTO -> field.setValue(fieldDTO.getValue()));
        }
    }

    private void updateOverridenMethodsTree(ClassPM classPMtoAdd){
        ClassPM currentNode = getObjectTree();
        List<MethodPM> duplicateMethodsToDelete = new ArrayList<>();

        while(currentNode != classPMtoAdd){
            ClassPM currentNodeStaticCopy = currentNode;
            classPMtoAdd.getMethods().forEach(methodOfClassPMtoAdd -> {
                Optional<MethodPM> duplicateMethod = currentNodeStaticCopy.getMethods().stream()
                        .filter(e -> e.equals(methodOfClassPMtoAdd))
                        .findFirst();

                if(duplicateMethod.isPresent()){
                    duplicateMethodsToDelete.add(duplicateMethod.get());
                    if(duplicateMethod.get().getImplementedInClass() != null){
                        methodOfClassPMtoAdd.setImplementedInClass(duplicateMethod.get().getImplementedInClass());
                    }
                    else{
                        methodOfClassPMtoAdd.setImplementedInClass(currentNodeStaticCopy.getFullClassName());
                    }
                }

            });
            currentNode = currentNode.getSuperClass();
        }

        deleteMethods(duplicateMethodsToDelete);

    }

    public void deleteMethods(List<MethodPM> methodsToDelete){

        ClassPM currentNode = getObjectTree();

        for(MethodPM method : methodsToDelete){
            currentNode.getMethods().remove(method);
        }

        while(currentNode.getSuperClass() != null){
            currentNode = currentNode.getSuperClass();
            for(MethodPM method : methodsToDelete){
                currentNode.getMethods().remove(method);

            }
        }

    }

    /**
     *
     * Set class where a method is implemented by traversing the class tree from most specified object part
     * first occurency of method is the implementation class the last occurency is the declaration class.
     *
     * all duplicate methods are deleted, only the class which declared the method keeps it.
     */

    private void updateOverridenMethods(){
        List<MethodPM> allMethods = objectParts.stream()
                .flatMap(e -> e.getMethods().stream())
                .collect(Collectors.toList());
        List<MethodPM> duplicateMethods = new ArrayList<>();

        for(MethodPM method : allMethods){
            List<MethodPM> methodAppearances =
                    allMethods.stream().filter(e -> e.equals(method)).collect(Collectors.toList());

            if(methodAppearances.size() > 1)   {
                boolean alreadyAdded  = false;
                //make sure that duplicate method is added only once
                for(MethodPM duplicateMethod : duplicateMethods){
                    if(method.equals(duplicateMethod)) alreadyAdded = true;
                }
                if(!alreadyAdded) {
                    duplicateMethods.add(method);
                }
            }
        }

        duplicateMethods.forEach(duplicateMethod -> {
            String implementedInClass = getImplementedInClass(duplicateMethod);

            boolean firstMethodDeclarationFound = false;

            List<MethodPM> methodsToDeleteInObject = new ArrayList<>();

            for (int i = objectParts.size(); i-- > 0; ) {
                for(MethodPM currentMethod : objectParts.get(i).getMethods()){
                    if(currentMethod.equals(duplicateMethod)){
                        if(!firstMethodDeclarationFound){
                            currentMethod.setImplementedInClass(implementedInClass);
                            firstMethodDeclarationFound = true;
                        }
                        else {
                            methodsToDeleteInObject.add(currentMethod);
                        }
                    }
                }
            }

            for(ClassPM objectPart : objectParts){
                for(MethodPM methodToDelete : methodsToDeleteInObject)
                    objectPart.getMethods().remove(methodToDelete);
            }

        });
    }

    private String getImplementedInClass(MethodPM method){
        for(ClassPM classPM : objectParts) {
            for(MethodPM methodPM : classPM.getMethods()){
                if(methodPM.equals(method)){
                    return classPM.getFullClassName();
                }
            }

        }
        return null;
    }

    public String getObjectId() {
        return objectId.get();
    }

    public StringProperty objectIdProperty() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId.set(objectId);
    }

    public ClassPM getObjectRootClass() {
        return objectRootClass.get();
    }

    public ObjectProperty<ClassPM> objectRootClassProperty() {
        return objectRootClass;
    }

    public void setObjectRootClass(ClassPM objectRootClass) {
        this.objectRootClass.set(objectRootClass);
    }

    public ClassPM getObjectTree() {
        return objectTree.get();
    }

    public ObjectProperty<ClassPM> objectTreeProperty() {
        return objectTree;
    }

    public void setObjectTree(ClassPM objectTree) {
        this.objectTree.set(objectTree);
    }

    public ObservableList<ClassPM> getObjectParts() {
        return objectParts;
    }

    public ObservableList<ReferencePM> getReferences() {
        return references;
    }

    public void addReference(ReferencePM reference){
        references.add(reference);
    }

    public int getObjectWidth() {
        return objectWidth.get();
    }

    public IntegerProperty objectWidthProperty() {
        return objectWidth;
    }

    public void setObjectWidth(int objectWidth) {
        this.objectWidth.set(objectWidth);
    }
}
