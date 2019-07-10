package service.uml;

import java.util.List;

public class ClassDTO {
    private final Boolean isInterface;
    private final String fullClassName;
    private final String simpleClassName;
    private final String superClassName;
    private final List<String> implementedInterfaces;
    private final List<String> fields;
    private final List<String> constructors;
    private final List<String> methods;

    public ClassDTO(Boolean isInterface,
                    String fullClassName,
                    String simpleClassName,
                    String superClassName,
                    List<String> implementedInterfaces,
                    List<String> fields,
                    List<String> constructors,
                    List<String> methods){

        this.isInterface = isInterface;
        this.fullClassName = fullClassName;
        this.simpleClassName = simpleClassName;
        this.superClassName = superClassName;
        this.implementedInterfaces = implementedInterfaces;
        this.fields = fields;
        this.constructors = constructors;
        this.methods = methods;
    }

    public Boolean isInterface() {
        return isInterface;
    }

    public String getSuperClassName() {
        return superClassName;
    }

    public List<String> getImplementedInterfaces() {
        return implementedInterfaces;
    }

    public String getFullClassName() {
        return fullClassName;
    }

    public String getSimpleClassName() {
        return simpleClassName;
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
