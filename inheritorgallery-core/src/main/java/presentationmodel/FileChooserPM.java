package presentationmodel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.stage.FileChooser;
import presentationmodel.uml.UmlPM;
import service.jshell.JShellService;

import java.nio.file.Path;

public class FileChooserPM {
    private final ObjectProperty<Path> path = new SimpleObjectProperty<>();
    private FileChooser fileChooser;
    private JShellService jShellService = JShellService.getInstance();
    private UmlPM umlPM;

    public FileChooserPM(UmlPM umlPM){

        this.umlPM = umlPM;
        fileChooser = new FileChooser();
    }

    public FileChooser getFileChooser() {
        return fileChooser;
    }

    private void propagatePath(){
        jShellService.updateImports(getPath());
        //todo: update UML + Instruction
        umlPM.init();
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
