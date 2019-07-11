package presentationmodel.instance;

import exceptions.InvalidCodeException;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentationmodel.uml.UmlPM;
import service.jshell.JShellService;
import service.jshell.ObjectDTO;
import service.jshell.ReferenceDTO;

import java.util.Optional;

public class InstanceStatePM {
    private static Logger logger = LoggerFactory.getLogger(InstanceStatePM.class);

    private JShellService jShellService = JShellService.getInstance();

    private final ObservableList<String > commandHistory = FXCollections.observableArrayList();
    private final ObservableList<ObjectPM> objectPMs = FXCollections.observableArrayList();
    // Temporary list to avoid getting a listener called multiple times when the list is being updated
    private final ObservableList<ObjectPM> objectPMsTemp = FXCollections.observableArrayList();
    private final ObservableList<ReferencePM > referencePMs = FXCollections.observableArrayList();

    private final SimpleListProperty objectPMProperty = new SimpleListProperty(objectPMs);

    private UmlPM umlPM;

    public InstanceStatePM(UmlPM umlPM){
        this.umlPM = umlPM;
    }

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
        objectPMsTemp.clear();
        for(ObjectDTO objectDTO : jShellService.getObjectDTOs() ){
            objectPMsTemp.add(
                    new ObjectPM(
                            umlPM,
                            objectDTO.getObjectId(),
                            objectDTO.getObjectFullName(),
                            objectDTO.getFieldValues()
            ));
        }
        objectPMs.setAll(objectPMsTemp);

        referencePMs.clear();
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

    public ListProperty objectPMProperty() {
        return objectPMProperty;
    }
}
