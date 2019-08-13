package service.jshell.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Data transfer object for Object
 */
public class ObjectDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String objectId;
    private String objectFullName; //e.g. Car, instatiated with "... = new Car()"
    private List<FieldDTO> fieldValues;

    /**
     * Create the ObjectDTO
     * @param objectId HashCodeID for the object
     * @param objectFullName The root class name including package where the object has been created from
     * @param fieldValues The values of the fields
     */
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
