package presentationmodel.instance;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class ObjectPM {

    private final StringProperty objectId = new SimpleStringProperty();
    private final StringProperty objectName = new SimpleStringProperty();

    public ObjectPM(String objectId, String objectName){
        setObjectId(objectId);
        setObjectName(objectName);
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

    public String getObjectName() {
        return objectName.get();
    }

    public StringProperty objectNameProperty() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName.set(objectName);
    }
}
