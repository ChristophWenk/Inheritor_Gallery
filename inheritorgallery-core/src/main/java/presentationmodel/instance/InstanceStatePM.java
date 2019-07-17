package presentationmodel.instance;

import exceptions.InvalidCodeException;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentationmodel.uml.ClassPM;
import presentationmodel.uml.MethodPM;
import presentationmodel.uml.UmlPM;
import service.jshell.JShellService;
import service.jshell.dto.ObjectDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InstanceStatePM {
    private static Logger logger = LoggerFactory.getLogger(InstanceStatePM.class);

    private JShellService jShellService = JShellService.getInstance();
    private final ObservableList<String > commandHistory = FXCollections.observableArrayList();
    private final ObjectProperty<List<ObjectPM>> objectPMProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<MethodPM> lastExecutedMethod = new SimpleObjectProperty<>();
    private UmlPM umlPM;

    public InstanceStatePM(UmlPM umlPM){
        this.umlPM = umlPM;
    }

    public void setJShellInput(String input) {

        try {
            String output = jShellService.getOutputAsString(jShellService.evaluateCode(input));
            commandHistory.addAll(input,output);
            updateInstances();
            updateLastExecutedMethod(input);
        } catch (InvalidCodeException e) {
            commandHistory.add("Code could not be interpreted by JShell. Please verify the statement.");
        }
    }

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

    private void updateLastLastExecutedMethodUML(Boolean lastExecuted){
        umlPM.getClasses().stream()
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

    private int getParamCountOfMethod(String code, String refName, String methodName){
        String[] stringAfterMethodName = code.split(refName+"\\."+methodName+"\\(");
        if(stringAfterMethodName.length > 1) {
            String stringInMethodBrackets = stringAfterMethodName[1].split("\\)")[0];
            if(stringInMethodBrackets.length() == 0 ) return 0;
            else    return stringInMethodBrackets.split(",").length;
        }
        return 0;
    }

    private void resetLastExecutedMethod(){
        if(getLastExecutedMethod() != null)  {
            getLastExecutedMethod().setLastExecuted(false);
            updateLastLastExecutedMethodUML(false);
        }
    }

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
