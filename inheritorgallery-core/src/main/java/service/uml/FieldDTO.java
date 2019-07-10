package service.uml;

public class FieldDTO {
    private final String accessType;
    private final String type;
    private final String name;

    public FieldDTO(String accessType,
                    String type,
                    String name){

        this.type = type;
        this.accessType = accessType;
        this.name = name;
    }

    public String getAccessType() {
        return accessType;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
