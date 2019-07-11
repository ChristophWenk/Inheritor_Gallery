package service.jshell;

import java.util.Map;

public class ObjectDTO {
    private String objectId;
    private String objectFullName; //e.g. Car, instatiated with "... = new Car()"

    public ObjectDTO(String objectId, String objectFullName){
        this.objectFullName = objectFullName;
        this.objectId = objectId;
    }

    public String getObjectFullName() {
        return objectFullName;
    }
    public String getObjectId() {
        return objectId;
    }
}
