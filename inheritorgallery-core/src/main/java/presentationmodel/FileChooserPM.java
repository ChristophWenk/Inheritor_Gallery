package presentationmodel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.stage.FileChooser;
import presentationmodel.instance.InstanceStatePM;
import presentationmodel.uml.UmlPM;
import service.jshell.JShellService;

import java.nio.file.Path;

public class FileChooserPM {
    private final ObjectProperty<Path> path = new SimpleObjectProperty<>();
    private FileChooser fileChooser;
    private JShellService jShellService = JShellService.getInstance();
    private UmlPM umlPM;
    private InstanceStatePM instanceStatePM;

    public FileChooserPM(UmlPM umlPM, InstanceStatePM instanceStatePM){
        this.umlPM = umlPM;
        this.instanceStatePM = instanceStatePM;
        this.fileChooser = new FileChooser();
    }

    public FileChooser getFileChooser() {
        return fileChooser;
    }

    private void propagatePath(){
        jShellService.updateImports(getPath());
        //todo: update UML + Instruction
        umlPM.init();
        instanceStatePM.setJShellInput(";");
    }

    public void reset(){
        umlPM.init();
        jShellService.reset();
        instanceStatePM.setJShellInput("\"refresh\";");

    }


    public Path getPath() {
        return path.get();
    }

    public ObjectProperty<Path> pathProperty() {
        return path;
    }

    public void setPath(Path path) {
        this.path.set(path);
        propagatePath();
    }
}
