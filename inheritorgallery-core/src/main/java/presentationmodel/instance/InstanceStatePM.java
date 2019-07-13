package presentationmodel.instance;

import exceptions.InvalidCodeException;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentationmodel.uml.UmlPM;
import service.jshell.JShellService;
import service.jshell.ObjectDTO;
import service.jshell.ReferenceDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InstanceStatePM {
    private static Logger logger = LoggerFactory.getLogger(InstanceStatePM.class);

    private JShellService jShellService = JShellService.getInstance();

    private final ObservableList<String > commandHistory = FXCollections.observableArrayList();
    private final ObjectProperty<List<ObjectPM>> objectPMProperty = new SimpleObjectProperty<>();

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
        List<ObjectPM> objectPMList = new ArrayList<>();
        for(ObjectDTO objectDTO : jShellService.getObjectDTOs() ){
            objectPMList.add(
                    new ObjectPM(
                            umlPM,
                            objectDTO.getObjectId(),
                            objectDTO.getObjectFullName(),
                            objectDTO.getFieldValues()
            ));
        }

        jShellService.getReferenceDTOs().forEach(referenceDTO ->
                objectPMList.stream().filter(objectPM -> objectPM.getObjectId().equals(referenceDTO.getPointedToObject()))
                   .forEach(e -> e.addReference(new ReferencePM(referenceDTO.getRefType(),referenceDTO.getRefName()))));


        setObjectPMProperty(objectPMList);
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
}
