package presentationmodel;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.FileChooser;
import presentationmodel.instance.InstanceStatePM;
import presentationmodel.instruction.InstructionPM;
import presentationmodel.uml.UmlPM;
import service.jshell.JShellService;

import java.nio.file.Path;

public class FileChooserPM {
    private final SimpleStringProperty pathAsString = new SimpleStringProperty();
    private final SimpleObjectProperty<Path> pathSimpleObjectProperty = new SimpleObjectProperty<>();
    private FileChooser fileChooser;
    private JShellService jShellService = JShellService.getInstance();
    private UmlPM umlPM;
    private InstanceStatePM instanceStatePM;
    private InstructionPM instructionPM;

    public FileChooserPM(UmlPM umlPM, InstanceStatePM instanceStatePM, InstructionPM instructionPM){
        this.instructionPM = instructionPM;
        this.umlPM = umlPM;
        this.instanceStatePM = instanceStatePM;
        this.fileChooser = new FileChooser();
    }

    public FileChooser getFileChooser() {
        return fileChooser;
    }

    private void propagatePath(){
        jShellService.updateImports(getPath());
        umlPM.init();
        instanceStatePM.setJShellInput(";");
        instanceStatePM.setJShellInput("\"Loading classes\";");
    }

    public void reset(){
        umlPM.init();
        jShellService.reset();
        instanceStatePM.setJShellInput("\"refresh\";");

    }

    public String getPath() {
        return pathAsString.get();
    }

    public void setPathAsString(String path) {
        this.pathAsString.set(path);
        propagatePath();
    }

    public void setPathSimpleObjectProperty(Path pathSimpleObjectProperty) {
        this.pathSimpleObjectProperty.set(pathSimpleObjectProperty);
        instructionPM.setInstructionText(pathSimpleObjectProperty);
    }
}
