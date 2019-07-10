package service.uml;

import java.util.List;

public class MethodDTO {
    private final String modifier;
    private final String returnType;
    private final String name;
    private List<String> inputParameters;

    public MethodDTO(String modifier, String returnType, String name, List<String> inputParameters){
        this.modifier = modifier;
        this.returnType = returnType;
        this.name = name;
        this.inputParameters = inputParameters;

    }

    public String getModifier() {
        return modifier;
    }

    public String getReturnType() {
        return returnType;
    }

    public String getName() {
        return name;
    }

    public List<String> getInputParameters() {
        return inputParameters;
    }
}
