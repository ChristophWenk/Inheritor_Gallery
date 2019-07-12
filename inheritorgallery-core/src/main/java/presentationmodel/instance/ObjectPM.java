package presentationmodel.instance;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentationmodel.uml.ClassPM;
import presentationmodel.uml.FieldPM;
import presentationmodel.uml.MethodPM;
import presentationmodel.uml.UmlPM;
import service.jshell.FieldDTO;

import java.lang.invoke.MethodHandle;
import java.util.*;
import java.util.stream.Collectors;


public class ObjectPM {

    private static Logger logger = LoggerFactory.getLogger(ObjectPM.class);

    private final StringProperty objectId = new SimpleStringProperty();
    private final StringProperty objectFullName = new SimpleStringProperty();
    private final ObservableList<ClassPM> objectParts = FXCollections.observableArrayList();
    private final ObservableList<ReferencePM> references = FXCollections.observableArrayList();

    private UmlPM umlPM;

    public ObjectPM(UmlPM umlPM, String objectId, String objectFullName, List<FieldDTO> fieldDTOs){
        this.umlPM = umlPM;
        setObjectId(objectId);
        setObjectFullName(objectFullName);

        setObjectStructure();
        setFieldValues(fieldDTOs);
        updateOverridenMethods();
    }

    public void setObjectStructure(){
        ClassPM rootClass = umlPM.getClassByFullName(objectFullName.getValue()).clone();

        objectParts.add(rootClass);

        while(rootClass.hasSuperClass()){
            rootClass  = umlPM.getClassByFullName(rootClass.getSuperClassName()).clone();
            objectParts.add(rootClass);
        }
    }

    private void setFieldValues(List<FieldDTO> fieldDTOs){
        for(ClassPM part : objectParts){
            for (FieldPM field : part.getFields()){
                Optional<FieldDTO> fieldOptional =  fieldDTOs.stream()
                        .filter(e -> e.getDeclaringClass().equals(part.getFullClassName()))
                        .filter(e -> e.getFieldName().equals(field.getName()))
                        .findFirst();
                fieldOptional.ifPresent(fieldDTO -> field.setValue(fieldDTO.getFieldValue()));
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
                allMethods.stream().filter(e -> isSameMethod(e,method)).collect(Collectors.toList());

            if(methodAppearances.size() > 1)   {
                boolean alreadyAdded  = false;
                //make sure that duplicate method is added only once
                for(MethodPM duplicateMethod : duplicateMethods){
                    if(isSameMethod(method,duplicateMethod)) alreadyAdded = true;
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
                    if(isSameMethod(currentMethod,duplicateMethod))   {
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
                if(isSameMethod(methodPM,method))   {
                    return classPM.getFullClassName();
                }
            }

        }
        return null;
    }

    public boolean isSameMethod(MethodPM m1, MethodPM m2){
        return  m1.getName().equals(m2.getName()) &&
                m1.getInputParameters().equals(m2.getInputParameters());
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

    public String getObjectFullName() {
        return objectFullName.get();
    }

    public StringProperty objectFullNameProperty() {
        return objectFullName;
    }

    public void setObjectFullName(String objectFullName) {
        this.objectFullName.set(objectFullName);
    }

    public ObservableList<ClassPM> getObjectParts() {
        return objectParts;
    }

    public ObservableList<ReferencePM> getReferences() {
        return references;
    }

    public void addReference(ReferencePM reference){

        logger.info("adding reference "+reference.getReferenceName() + " to " + this.getObjectFullName());
        references.add(reference);
    }
}
