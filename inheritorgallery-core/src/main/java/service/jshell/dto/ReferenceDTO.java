package service.jshell.dto;

import java.io.Serializable;

/**
 * Data transfer object for Reference
 */
public class ReferenceDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String refType;
    private String refName;
    private String pointedToObject;

    /**
     * Create the ReferenceDTO
     * @param refType ObjectType of a reference
     * @param refName Name of a reference
     * @param pointedToObject The object the reference points to
     */
    public ReferenceDTO(String refType, String refName, String pointedToObject){
        this.refType = refType;
        this.refName = refName;
        this.pointedToObject = pointedToObject;
    }
    public String getRefType() {
        return refType;
    }

    public String getRefName() {
        return refName;
    }

    public String getPointedToObject() {
        return pointedToObject;
    }
}
