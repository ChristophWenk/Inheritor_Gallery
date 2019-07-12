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

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class ObjectPM {

    private static Logger logger = LoggerFactory.getLogger(ObjectPM.class);

    private final StringProperty objectId = new SimpleStringProperty();
    private final StringProperty objectFullName = new SimpleStringProperty();
    private final ObservableList<ClassPM> objectParts = FXCollections.observableArrayList();

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
        ClassPM rootClass = umlPM.getClassByFullName(objectFullName.getValue());

        objectParts.add(rootClass);
        while(rootClass.hasSuperClass()){
            rootClass  = umlPM.getClassByFullName(rootClass.getSuperClassName());
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
        //iterate over all methods and set implementedInClass and declaredInClass
        //todo: store methods with same name and parameter as already implementation set


        objectParts.stream().flatMap(e -> e.getMethods().stream()).forEach(method -> {
            boolean implementationSet = false;

            for(ClassPM classPM : objectParts) {
                if(classPM.getMethods().contains(method)){
                    if(!implementationSet) {
                        method.setImplementedInClass(classPM.getFullClassName());
                        implementationSet = true;
                        logger.info("implemen: "+classPM.getFullClassName()+" "+method.getName());

                    }
                    logger.info("declared: "+classPM.getFullClassName()+" "+method.getName());
                    method.setDeclaredInClass(classPM.getFullClassName());
                }
            }
        });

        for(int i = objectParts.size() - 1; i >= 0; i--){
            //logger.info(objectParts.get(i).getFullClassName());
        }
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
}
