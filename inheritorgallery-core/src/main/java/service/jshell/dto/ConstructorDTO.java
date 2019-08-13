package service.jshell.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Data transfer object for constructors
 */
public class ConstructorDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String modifier;
    private final String name;
    private List<String> inputParameters;

    /**
     * Create the ConstructorDTO
     * @param modifier Modifier (e.g. public) for the constructor
     * @param name Name of the constructor
     * @param inputParameters List of input parameters of the constructor
     */
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
