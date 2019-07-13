package service.uml;

import java.util.List;

public class ConstructorDTO {
    private final String modifier;
    private final String name;
    private List<String> inputParameters;

    public ConstructorDTO(String modifier, String name, List<String> inputParameters){
        this.modifier = modifier;
        this.name = name;
        this.inputParameters = inputParameters;
    }

    public String getModifier() {
        return modifier;
    }

    public String getName() {
        return name;
    }

    public List<String> getInputParameters() {
        return inputParameters;
    }
}
