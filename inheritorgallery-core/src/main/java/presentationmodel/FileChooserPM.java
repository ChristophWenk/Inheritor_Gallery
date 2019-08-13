package presentationmodel;

import javafx.beans.property.SimpleObjectProperty;
import javafx.stage.FileChooser;
import presentationmodel.instance.InstanceStatePM;
import presentationmodel.instruction.InstructionPM;
import presentationmodel.uml.UmlPM;
import service.jshell.JShellService;

import java.nio.file.Path;

/**
 * Presentationmodel that stores the current state of the FileChooser
 */
public class FileChooserPM {
    private final SimpleObjectProperty<Path> pathSimpleObjectProperty = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Path> pathForClasses = new SimpleObjectProperty<>();
    private FileChooser fileChooser;
    private JShellService jShellService = JShellService.getInstance();
    private UmlPM umlPM;
    private InstanceStatePM instanceStatePM;
    private InstructionPM instructionPM;

    /**
     * Create the FileChooserPM
     * @param instructionPM The InstructionPM that the instruction text should be set for
     * @param umlPM The UmlPM that the classes should be loaded into
     * @param instanceStatePM The InstanceStatePM that will inform the user about the changes
     */
    public FileChooserPM(InstructionPM instructionPM, UmlPM umlPM, InstanceStatePM instanceStatePM){
        this.instructionPM = instructionPM;
        this.umlPM = umlPM;
        this.instanceStatePM = instanceStatePM;
        this.fileChooser = new FileChooser();
    }

    /**
     * Set new path for the newly loaded classes
     */
    private void propagatePath(){
        jShellService.updateImports(getPathForClasses());
        umlPM.init();
        instanceStatePM.setJShellInput(";");
        instanceStatePM.setJShellInput("\"Loading classes\";");
    }

    /**
     * Reset the state of the application
     */
    public void reset(){
        umlPM.init();
        jShellService.reset();
        instanceStatePM.setJShellInput("\"refresh\";");
    }

    public FileChooser getFileChooser() {
        return fileChooser;
    }

    public Path getPathSimpleObjectProperty() {
        return pathSimpleObjectProperty.get();
    }

    public SimpleObjectProperty<Path> pathSimpleObjectPropertyProperty() {
        return pathSimpleObjectProperty;
    }

    public void setPathSimpleObjectProperty(Path pathSimpleObjectProperty) {
        this.pathSimpleObjectProperty.set(pathSimpleObjectProperty);
        instructionPM.setInstructionText(pathSimpleObjectProperty);
    }

    public Path getPathForClasses() {
        return pathForClasses.get();
    }

    public SimpleObjectProperty<Path> pathForClassesProperty() {
        return pathForClasses;
    }

    public void setPathForClasses(Path pathForClasses) {
        this.pathForClasses.set(pathForClasses);
        propagatePath();
    }
}
