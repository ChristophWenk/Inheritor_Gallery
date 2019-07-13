package service.jshell.dto;

public class ReferenceDTO {
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
