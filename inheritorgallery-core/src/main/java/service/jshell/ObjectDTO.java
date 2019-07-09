package service.jshell;

import java.util.Map;

public class ObjectDTO {
    private String objectId;
    private String objectName; //e.g. Car, instatiated with "... = new Car()"

    public ObjectDTO(String objectId, String objectName){
        this.objectName = objectName;
        this.objectId = objectId;
    }

    public String getObjectName() {
        return objectName;
    }
    public String getObjectId() {
        return objectId;
    }
}
