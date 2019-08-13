package service.jshell.dto;

import java.io.Serializable;

/**
 * Data transfer object for field
 */
public class FieldDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String declaringClass;
    private final String modifier;
    private final String type;
    private final String name;
    private final String value;

    /**
     * Create the FieldDTO
     * @param declaringClass The class that declares the field
     * @param modifier Modifier of the field (e.g. public)
     * @param type Type of the field (e.g. static)
     * @param name Name of the field
     * @param value Value of the field
     */
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