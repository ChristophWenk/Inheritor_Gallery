package presentationmodel.instance;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class ObjectPM {

    private final StringProperty objectId = new SimpleStringProperty();
    private final StringProperty objectFullName = new SimpleStringProperty();

    public ObjectPM(String objectId, String objectFullName){
        setObjectId(objectId);
        setObjectFullName(objectFullName);
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
