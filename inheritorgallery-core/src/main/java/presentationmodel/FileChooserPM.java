package presentationmodel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.stage.FileChooser;
import service.jshell.JShellService;

import java.nio.file.Path;

public class FileChooserPM {
    private final ObjectProperty<Path> path = new SimpleObjectProperty<>();
    private FileChooser fileChooser;
    private JShellService jShellService = JShellService.getInstance();

    public FileChooserPM(){
        fileChooser = new FileChooser();
    }

    public FileChooser getFileChooser() {
        return fileChooser;
    }

    private void propagatePath(){
        jShellService.updateImports(getPath());
        //todo: update UML + Instruction

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
