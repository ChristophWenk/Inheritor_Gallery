package service.jshell;

public class FieldDTO {
    private String declaringClass;
    private String name;
    private String value;

    public FieldDTO(String declaringClass, String fieldName, String fieldValue ){
        this.declaringClass = declaringClass;
        this.name = fieldName;
        this.value = fieldValue;
    }

    public String getDeclaringClass() {
        return declaringClass;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
