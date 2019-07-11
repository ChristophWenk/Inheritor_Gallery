package presentationmodel.instance;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentationmodel.uml.ClassPM;
import presentationmodel.uml.UmlPM;


public class ObjectPM {

    private static Logger logger = LoggerFactory.getLogger(ObjectPM.class);

    private final StringProperty objectId = new SimpleStringProperty();
    private final StringProperty objectFullName = new SimpleStringProperty();
    private final ObservableList<ClassPM> objectParts = FXCollections.observableArrayList();

    private UmlPM umlPM;

    public ObjectPM(UmlPM umlPM, String objectId, String objectFullName){
        this.umlPM = umlPM;
        setObjectId(objectId);
        setObjectFullName(objectFullName);

        setObjectStructure();
    }

    public void setObjectStructure(){
        logger.info(objectFullName.getValue());
        ClassPM rootClass = umlPM.getClassByFullName(objectFullName.getValue());

        while(rootClass.hasSuperClass()){
            ;
            rootClass  = umlPM.getClassByFullName(rootClass.getSuperClassName());
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
}
