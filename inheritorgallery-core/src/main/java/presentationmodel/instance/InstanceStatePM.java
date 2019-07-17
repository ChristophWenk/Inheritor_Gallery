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
        logger.info(lastCodeInput);

        List<ReferencePM> refsFoundInCode = getAllReferenceNames().stream()
                .filter(referencePM -> lastCodeInput.contains(referencePM.getReferenceName()+"."))
                .collect(Collectors.toList());
        // exactly one ref must be found. if multiple refs are found no lastExecutedMethod is set
        if(refsFoundInCode.size() == 1){
            ReferencePM refInCode = refsFoundInCode.get(0);
            logger.info(refInCode.getReferenceName());

            Optional<ObjectPM> objectRefPointedTo = getObjectPMs().stream()
                    .filter(objectPM ->
                            objectPM.getReferences().stream().anyMatch(referencePM -> referencePM.equals(refInCode)))
                    .findFirst();

            if(objectRefPointedTo.isPresent()){
                List<MethodPM> methodsOfObject =  objectRefPointedTo.get().getAllObjectPartsFlat().stream()
                        .flatMap(o -> o.getMethods().stream())
                        .collect(Collectors.toList());

                //todo: add count of parameters in filter for overloaded methods
                List<MethodPM> methodsLatExecutedList =  methodsOfObject.stream()
                        .filter(methodPM -> lastCodeInput.contains(refInCode.getReferenceName()+"."+methodPM.getName()))
                        .collect(Collectors.toList());
                if(methodsLatExecutedList.size() == 1){
                    //previously executed method not last executed any more
                    if(getLastExecutedMethod() != null)  {
                        getLastExecutedMethod().setLastExecuted(false);
                        //update lastExecutedMethod in UML
                        umlPM.getClasses().stream()
                                .flatMap(classPM -> classPM.getMethods().stream())
                                .filter(methodPM -> methodPM.equals(getLastExecutedMethod()))
                                .forEach(methodPM -> methodPM.setLastExecuted(false));
                    }

                    setLastExecutedMethod(methodsLatExecutedList.get(0));
                    getLastExecutedMethod().setLastExecuted(true);
                    //update lastExecutedMethod in UML
                    umlPM.getClasses().stream()
                            .flatMap(classPM -> classPM.getMethods().stream())
                            .filter(methodPM -> methodPM.equals(getLastExecutedMethod()))
                            .forEach(methodPM -> methodPM.setLastExecuted(true));

                    logger.info(methodsLatExecutedList.get(0).getName());
                }



            }
        }
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
