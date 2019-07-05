package presentationmodel.jshell;

import exceptions.InvalidCodeException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import service.jshell.JShellService;

public class JShellPM {
    private final ObservableList<String > commandHistory = FXCollections.observableArrayList();
    private JShellService jShellService = JShellService.getInstance();


    public void setInput(String input) {

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
