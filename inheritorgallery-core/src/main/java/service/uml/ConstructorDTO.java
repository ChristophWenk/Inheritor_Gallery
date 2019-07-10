package service.uml;

import java.util.List;

public class ConstructorDTO {
    private final String accessType;
    private final String name;
    private List<String> inputParameters;

    public ConstructorDTO(String accessType,String name, List<String> inputParameters){
        this.accessType = accessType;
        this.name = name;
        this.inputParameters = inputParameters;
    }

    public String getAccessType() {
        return accessType;
    }

    public String getName() {
        return name;
    }

    public List<String> getInputParameters() {
        return inputParameters;
    }
}
