package service.uml;

public class FieldDTO {
    private final String fieldAccess;
    private final String fieldType;
    private final String fieldName;

    public FieldDTO(String fieldAccess,
                    String fieldType,
                    String fieldName){

        this.fieldType = fieldType;
        this.fieldAccess = fieldAccess;
        this.fieldName = fieldName;
    }

    public String getFieldAccess() {
        return fieldAccess;
    }

    public String getFieldType() {
        return fieldType;
    }

    public String getFieldName() {
        return fieldName;
    }
}
