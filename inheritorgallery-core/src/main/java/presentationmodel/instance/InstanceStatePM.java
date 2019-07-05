package presentationmodel.instance;

import exceptions.InvalidCodeException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import service.jshell.JShellService;

public class InstanceStatePM {
    private final ObservableList<String > commandHistory = FXCollections.observableArrayList();
    private final ObservableList<ObjectPM > objectPMS = FXCollections.observableArrayList();
    private final ObservableList<ReferencePM > referencePMS = FXCollections.observableArrayList();

    JShellService jShellService = JShellService.getInstance();

    public void setJShellInput(String input) {

        try {
            String output = jShellService.getOutputAsString(jShellService.evaluateCode(input));
            commandHistory.addAll(input,output);

        } catch (InvalidCodeException e) {
            commandHistory.add("Code could not be interpreted by JShell. Please verify the statement.");
        }
    }

    public ObservableList<String> getCommandHistory() {
        return commandHistory;
    }

}
