package service.jshell;

public class FieldDTO {
    private String declaringClass;
    private String fieldName;
    private String fieldValue;

    public FieldDTO(String declaringClass, String fieldName, String fieldValue ){
        this.declaringClass = declaringClass;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getDeclaringClass() {
        return declaringClass;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }
}
