package service.jshell.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Data transfer object for method
 */
public class MethodDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String modifier;
    private final String returnType;
    private final String name;
    private List<String> inputParameters;

    /**
     * Create the MethodDTO
     * @param modifier Modifier of the method (e.g. public)
     * @param returnType Return type of the method (e.g. String)
     * @param name Name of the method
     * @param inputParameters Parameters that may be passed to the method
     */
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
