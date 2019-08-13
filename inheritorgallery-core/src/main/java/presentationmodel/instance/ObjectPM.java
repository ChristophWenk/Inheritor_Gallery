package presentationmodel.instance;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentationmodel.uml.ClassPM;
import presentationmodel.uml.FieldPM;
import presentationmodel.uml.MethodPM;
import service.jshell.dto.FieldDTO;

import java.util.*;

/**
 * Presentationmodel that stores the current state of an instance
 */
public class ObjectPM {
    private static Logger logger = LoggerFactory.getLogger(ObjectPM.class);

    private final StringProperty objectId = new SimpleStringProperty();
    private final ObjectProperty<ClassPM> objectTree = new SimpleObjectProperty<>();
    private final ObservableList<ReferencePM> references = FXCollections.observableArrayList();

    /**
     * Create the ObjectPM
     * @param objectId HashCodeID for the object
     * @param objectRootClass The root class where the object has been created from
     * @param fieldDTOs The FieldDTOs holding the field values to be set
     */
    public ObjectPM(String objectId, ClassPM objectRootClass, List<FieldDTO> fieldDTOs){
        setObjectId(objectId);
        generateObjectTree(objectRootClass,fieldDTOs);
    }

    /**
     * Generate an inheritance tree out of the superclasses of the root class
     * @param objectRootClass The base class where the tree should be built from
     * @param fieldDTOs The FieldDTOs holding the field values to be set
     */
    private void generateObjectTree(ClassPM objectRootClass, List<FieldDTO> fieldDTOs){
        setObjectTree(objectRootClass.clone());

        ClassPM currentNode = getObjectTree();

        while (true){
            setFieldValues(currentNode,fieldDTOs);
            addInterfacesToNode(currentNode);
            updateOverwrittenMethodsTree(currentNode);

            //setup next iteration
            if(objectRootClass.hasSuperClass()){
                currentNode.setSuperClass(objectRootClass.getSuperClass().clone());
                currentNode = currentNode.getSuperClass();
                objectRootClass = objectRootClass.getSuperClass();
            }
            else break;
        }
    }

    /**
     * Add the implemented interfaces to a ClassPM
     * @param currentNode The object part under construction
     */
    private void addInterfacesToNode(ClassPM currentNode){
        //add interfaces
        if(currentNode.getImplementedInterfaces() != null){
            List<ClassPM> classClones = new ArrayList<>();
            for(ClassPM classPM : currentNode.getImplementedInterfaces()){
                ClassPM classClone = classPM.clone();
                classClones.add(classClone);
            }
            currentNode.setImplementedInterfaces(classClones);
        }
    }

    /**
     * Set the field values for a class part of the object
     * @param part The object part under construction
     * @param fieldDTOs The FieldDTOs holding the value information
     */
    private void setFieldValues(ClassPM part, List<FieldDTO> fieldDTOs){
        for (FieldPM field : part.getFields()){
            Optional<FieldDTO> fieldOptional =  fieldDTOs.stream()
                    .filter(e -> e.getDeclaringClass().equals(part.getFullClassName()))
                    .filter(e -> e.getName().equals(field.getName()))
                    .findFirst();
            fieldOptional.ifPresent(fieldDTO -> field.setValue(fieldDTO.getValue()));
        }
    }

    /**
     * Check for overwritten methods and remove them from nodes where they are not used
     * @param classPMtoAdd The object part under construction
     */
    private void updateOverwrittenMethodsTree(ClassPM classPMtoAdd){
        ClassPM currentNode = getObjectTree();

        List<MethodPM> duplicateMethodsToDelete = new ArrayList<>();

        //check interfaces first
        while(currentNode.getImplementedInterfaces() != null){
            for(ClassPM currentInterface: currentNode.getImplementedInterfaces()){
                updateMethodImplementedIn(currentNode, currentInterface.getMethods());
                duplicateMethodsToDelete.addAll(
                        getMethodsToDelete(currentNode, currentInterface.getMethods())
                );
            }

            if(currentNode.getSuperClass() != null)  currentNode = currentNode.getSuperClass();
            else break;
        }

        deleteMethods(duplicateMethodsToDelete);

        //start from top again for classes
        currentNode = getObjectTree();

        while(currentNode != classPMtoAdd){
            updateMethodImplementedIn(currentNode, classPMtoAdd.getMethods());
            duplicateMethodsToDelete.addAll(
                    getMethodsToDelete(currentNode, classPMtoAdd.getMethods())
            );
            currentNode = currentNode.getSuperClass();
        }

        deleteMethods(duplicateMethodsToDelete);
    }

    /**
     * Get duplicate methods that are overwritten
     * @param classPM A root object part of the object part under construction
     * @param methods List of methods of the object part under construction
     * @return
     */
    private List<MethodPM> getMethodsToDelete(ClassPM classPM, List<MethodPM> methods){
        List<MethodPM> duplicateMethodsToDelete = new ArrayList<>();

        methods.forEach(methodOfClassPMtoAdd -> {
            Optional<MethodPM> duplicateMethod = classPM.getMethods().stream()
                    .filter(e -> e.equals(methodOfClassPMtoAdd))
                    .findFirst();

            duplicateMethod.ifPresent(duplicateMethodsToDelete::add);

        });
        return duplicateMethodsToDelete;
    }

    /**
     * Check for duplicate methods and mark where they are implemented eventually
     * @param classPM The node under construction
     * @param methods The list of methods that needs to be checked for duplicates
     */
    private void updateMethodImplementedIn(ClassPM classPM, List<MethodPM> methods){
        methods.forEach(methodOfClassPMtoAdd -> {
            Optional<MethodPM> duplicateMethod = classPM.getMethods().stream()
                    .filter(e -> e.equals(methodOfClassPMtoAdd))
                    .findFirst();

            if(duplicateMethod.isPresent()){

                if(duplicateMethod.get().getImplementedInClass() != null){
                    methodOfClassPMtoAdd.setImplementedInClass(duplicateMethod.get().getImplementedInClass());
                }
                else{
                    methodOfClassPMtoAdd.setImplementedInClass(classPM.getFullClassName());
                }
            }
        });
    }

    /**
     * Delete unused methods
     * @param methodsToDelete The list of methods to delete
     */
    private void deleteMethods(List<MethodPM> methodsToDelete){

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

    public String getObjectId() {
        return objectId.get();
    }

    public StringProperty objectIdProperty() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId.set(objectId);
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

    public ObservableList<ReferencePM> getReferences() {
        return references;
    }

    public void addReference(ReferencePM reference){
        references.add(reference);
    }

    public List<ClassPM> getAllObjectPartsFlat(){
        ClassPM currentNode = getObjectTree();
        List<ClassPM> allObjectPartsFlat = new ArrayList<>();
        //getObjectTree().
        while (true){
            allObjectPartsFlat.add(currentNode);
            if(currentNode.getImplementedInterfaces().size() > 0)
                allObjectPartsFlat.addAll(currentNode.getImplementedInterfaces());

            //setup next iteration
            if(currentNode.hasSuperClass()) currentNode = currentNode.getSuperClass();
            else break;
        }

        return allObjectPartsFlat;
    }
}
