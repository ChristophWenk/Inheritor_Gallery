package service.uml;

public class FieldDTO {
    private final String declaringClass;
    private final String modifier;
    private final String type;
    private final String name;
    private final String value;

    public FieldDTO(String declaringClass,
                    String modifier,
                    String type,
                    String name,
                    String value){

        this.declaringClass = declaringClass;
        this.type = type;
        this.modifier = modifier;
        this.name = name;
        this.value = value;
    }

    public String getDeclaringClass() {
        return declaringClass;
    }

    public String getModifier() {
        return modifier;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
