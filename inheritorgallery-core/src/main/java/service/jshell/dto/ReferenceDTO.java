package service.jshell.dto;

import java.io.Serializable;

public class ReferenceDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String refType;
    private String refName;
    private String pointedToObject;

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
