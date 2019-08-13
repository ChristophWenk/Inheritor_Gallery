package presentationmodel.instance;

import exceptions.InvalidCodeException;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentationmodel.uml.MethodPM;
import presentationmodel.uml.UmlPM;
import service.jshell.JShellService;
import service.jshell.dto.ObjectDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Presentationmodel that handles the current state of all instances
 */
public class InstanceStatePM {
    private static Logger logger = LoggerFactory.getLogger(InstanceStatePM.class);

    private JShellService jShellService = JShellService.getInstance();
    // Code commands given by the user and JShell output
    private final ObservableList<String> commandHistory = FXCollections.observableArrayList();
    private final ObjectProperty<List<ObjectPM>> objectPMProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<MethodPM> lastExecutedMethod = new SimpleObjectProperty<>();
    private UmlPM umlPM;

    /**
     * Create the InstanceStatePM
     * @param umlPM The UmlPM the InstanceStatePM is based on
     */
    public InstanceStatePM(UmlPM umlPM){
        this.umlPM = umlPM;
    }

    /**
     * Handle a new input for the JShell
     * @param input JShell input as given by the user
     */
    public void setJShellInput(String input) {
        try {
            String output = jShellService.getOutputAsString(jShellService.evaluateCode(input));
            commandHistory.addAll(input,output);
            jShellService.checkforDeletion();
            updateInstances();
            updateLastExecutedMethod(input);
        } catch (InvalidCodeException e) {
            commandHistory.add("Code could not be interpreted by JShell. Please verify the statement.");
        }
    }

    /**
     * Update all existing instances after a new input has been processed
     */
    private void updateInstances(){
        List<ObjectPM> objectPMList = new ArrayList<>();
        for(ObjectDTO objectDTO : jShellService.getObjectDTOs() ){
            objectPMList.add(
                    new ObjectPM(
                            objectDTO.getObjectId(),
                            umlPM.getClassByFullName(objectDTO.getObjectFullName()),
                            objectDTO.getFieldValues()
            ));
        }

        jShellService.getReferenceDTOs().forEach(referenceDTO ->
                objectPMList.stream().filter(objectPM -> objectPM.getObjectId().equals(referenceDTO.getPointedToObject()))
                   .forEach(e -> e.addReference(new ReferencePM(referenceDTO.getRefType(),referenceDTO.getRefName()))));

        setObjectPMProperty(objectPMList);
    }

    /**
     * Mark the last executed method in instance and UML
     * @param lastCodeInput JShell input as given by the user
     */
    private void updateLastExecutedMethod(String lastCodeInput){
        resetLastExecutedMethod();

        List<ReferencePM> refsFoundInCodeList = getAllReferenceNames().stream()
                .filter(referencePM -> lastCodeInput.contains(referencePM.getReferenceName()+"."))
                .collect(Collectors.toList());
        // exactly one ref must be found. if multiple refs are found no lastExecutedMethod is set
        if(refsFoundInCodeList.size() == 1){
            ReferencePM refInCode = refsFoundInCodeList.get(0);

            // get all methods of object where the reference points to
            List<MethodPM> methodsLastExecutedList =  getMethodsOfReference(refInCode).stream()
                    // check for String in code like "refName.methodName("
                    .filter(methodPM -> lastCodeInput.contains(refInCode.getReferenceName()+"."+methodPM.getName()+"("))
                    // check for param count to pick the right method by method overloading
                    .filter(methodPM -> methodPM.getInputParameters().size() ==
                            getParamCountOfMethod(lastCodeInput, refInCode.getReferenceName(), methodPM.getName()))
                    .collect(Collectors.toList());
            if(methodsLastExecutedList.size() == 1){ //exactly one matching method must be found
                setLastExecutedMethod(methodsLastExecutedList.get(0));
                getLastExecutedMethod().setLastExecuted(true);
                //update lastExecutedMethod in UML
                updateLastLastExecutedMethodUML(true);
            }
        }
    }

    /**
     * Mark the last executed method in UML
     * @param lastExecuted Flag that globally sets the last found executed method
     */
    private void updateLastLastExecutedMethodUML(Boolean lastExecuted){
        umlPM.getClassesObject().stream()
                .flatMap(c -> c.getMethods().stream())
                .filter(methodPM ->
                        // if method has been overridden, call equals with declaredInClass param
                        // to modify only the method that overrides = where the implementation is used
                          (getLastExecutedMethod().getImplementedInClass() != null) ?
                                 methodPM.equals(getLastExecutedMethod(),
                                         umlPM.getClassByFullName(getLastExecutedMethod().getImplementedInClass()))   :
                                 methodPM.equals(getLastExecutedMethod())
                        )
                .forEach(methodPM -> methodPM.setLastExecuted(lastExecuted));
    }

    /**
     * Get the amount of parameters for a method
     * @param code JShell input as given by the user
     * @param refName The name of the reference
     * @param methodName The name of the method
     * @return The amount of parameters
     */
    private int getParamCountOfMethod(String code, String refName, String methodName){
        String[] stringAfterMethodName = code.split(refName+"\\."+methodName+"\\(");
        if(stringAfterMethodName.length > 1) {
            String stringInMethodBrackets = stringAfterMethodName[1].split("\\)")[0];
            if(stringInMethodBrackets.length() == 0 ) return 0;
            else    return stringInMethodBrackets.split(",").length;
        }
        return 0;
    }

    /**
     * Reset the last executed method marking
     */
    private void resetLastExecutedMethod(){
        if(getLastExecutedMethod() != null)  {
            getLastExecutedMethod().setLastExecuted(false);
            updateLastLastExecutedMethodUML(false);
        }
    }

    /**
     * Get all methods for a specific reference
     * @param refInCode The reference for which the methods should be retrieved
     * @return List of MethodPMs with all the methods for the reference
     */
    private List<MethodPM> getMethodsOfReference(ReferencePM refInCode){
        List<MethodPM> methodsOfObject = new ArrayList<>();

        Optional<ObjectPM> objectRefPointedTo = getObjectPMs().stream()
                .filter(objectPM ->
                        objectPM.getReferences().stream().anyMatch(referencePM -> referencePM.equals(refInCode)))
                .findFirst();

        if(objectRefPointedTo.isPresent()){
            methodsOfObject =  objectRefPointedTo.get().getAllObjectPartsFlat().stream()
                    .flatMap(o -> o.getMethods().stream())
                    .collect(Collectors.toList());
        }
        return methodsOfObject;
    }

    /**
     * Get all names for all references
     * @return List of ReferencePMs with all currently existing reference names
     */
    private List<ReferencePM> getAllReferenceNames(){
        return getObjectPMs().stream()
                .flatMap(objectPM -> objectPM.getReferences().stream())
                .collect(Collectors.toList());
    }

    public ObservableList<String> getCommandHistory() {
        return commandHistory;
    }

    public List<ObjectPM> getObjectPMs() {
        return objectPMProperty.get();
    }

    public ObjectProperty<List<ObjectPM>> objectPMProperty() {
        return objectPMProperty;
    }

    public void setObjectPMProperty(List<ObjectPM> objectPMProperty) {
        this.objectPMProperty.set(objectPMProperty);
    }

    public MethodPM getLastExecutedMethod() {
        return lastExecutedMethod.get();
    }

    public ObjectProperty<MethodPM> lastExecutedMethodProperty() {
        return lastExecutedMethod;
    }

    public void setLastExecutedMethod(MethodPM lastExecutedMethod) {
        this.lastExecutedMethod.set(lastExecutedMethod);
    }
}
