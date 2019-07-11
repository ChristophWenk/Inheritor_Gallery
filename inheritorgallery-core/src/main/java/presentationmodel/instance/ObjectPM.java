package presentationmodel.instance;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentationmodel.uml.ClassPM;
import presentationmodel.uml.FieldPM;
import presentationmodel.uml.UmlPM;
import service.jshell.FieldDTO;

import java.util.List;
import java.util.Optional;


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
