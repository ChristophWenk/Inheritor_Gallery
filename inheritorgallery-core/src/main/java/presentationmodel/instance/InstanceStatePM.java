package presentationmodel.instance;

import exceptions.InvalidCodeException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import service.jshell.JShellService;
import service.jshell.ObjectDTO;
import service.jshell.ReferenceDTO;
import java.util.Optional;

public class InstanceStatePM {
    private final ObservableList<String > commandHistory = FXCollections.observableArrayList();
    private final ObservableList<ObjectPM> objectPMs = FXCollections.observableArrayList();
    private final ObservableList<ReferencePM > referencePMs = FXCollections.observableArrayList();

    JShellService jShellService = JShellService.getInstance();

    public void setJShellInput(String input) {

        try {
            String output = jShellService.getOutputAsString(jShellService.evaluateCode(input));
            commandHistory.addAll(input,output);
            updateInstances();
        } catch (InvalidCodeException e) {
            commandHistory.add("Code could not be interpreted by JShell. Please verify the statement.");
        }
    }

    private void updateInstances(){
        objectPMs.removeAll();
        for(ObjectDTO objectDTO : jShellService.getObjectDTOs() ){
            objectPMs.add(new ObjectPM(
                    objectDTO.getObjectId(),
                    objectDTO.getObjectName()
            ));
        }
        referencePMs.removeAll();
        for(ReferenceDTO referenceDTO : jShellService.getReferenceDTOs()){
            Optional<ObjectPM> ObjectPMPointedTo = objectPMs.stream()
                    .filter(objectPM -> objectPM.getObjectId().equals(referenceDTO.getPointedToObject()))
                    .findFirst();
            if(ObjectPMPointedTo.isPresent()){
                referencePMs.add(new ReferencePM(
                        referenceDTO.getRefType(),
                        referenceDTO.getRefName(),
                        ObjectPMPointedTo.get()
                ));
            }
        }
    }

    public ObservableList<String> getCommandHistory() {
        return commandHistory;
    }

    public ObservableList<ObjectPM> getObjectPMs() {
        return objectPMs;
    }

    public ObservableList<ReferencePM> getReferencePMs() {
        return referencePMs;
    }
}
