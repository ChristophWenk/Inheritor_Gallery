package service.uml;

public class FieldDTO {
    private final String modifier;
    private final String type;
    private final String name;

    public FieldDTO(String modifier,
                    String type,
                    String name){

        this.type = type;
        this.modifier = modifier;
        this.name = name;
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
}
