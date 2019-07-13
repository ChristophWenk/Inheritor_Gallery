package service.jshell.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ObjectDTO {
    private String objectId;
    private String objectFullName; //e.g. Car, instatiated with "... = new Car()"
    private List<FieldDTO> fieldValues;
    private static Logger logger = LoggerFactory.getLogger(ObjectDTO.class);

    public ObjectDTO(String objectId, String objectFullName, List<FieldDTO> fieldValues){
        this.objectFullName = objectFullName;
        this.objectId = objectId;
        this.fieldValues = fieldValues;
    }

    public String getObjectFullName() {
        return objectFullName;
    }
    public String getObjectId() {
        return objectId;
    }

    public List<FieldDTO> getFieldValues() {
        return fieldValues;
    }
}
