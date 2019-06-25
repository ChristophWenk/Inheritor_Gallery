package service.uml;

import java.util.List;

public class ClassDTO {
    private final String name;
    private final List<String> fields;
    private final List<String> constructors;
    private final List<String> methods;

    public ClassDTO(String name, List<String> fields, List<String> constructors, List<String> methods){
        this.name = name;
        this.fields = fields;
        this.constructors = constructors;
        this.methods = methods;
    }

    public String getName() {
        return name;
    }

    public List<String> getFields() {
        return fields;
    }

    public List<String> getConstructors() {
        return constructors;
    }

    public List<String> getMethods() {
        return methods;
    }
}
