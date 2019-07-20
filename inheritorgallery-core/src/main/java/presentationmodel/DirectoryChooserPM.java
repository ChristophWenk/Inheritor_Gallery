package presentationmodel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.stage.DirectoryChooser;
import service.jshell.JShellService;

import java.nio.file.Path;

public class DirectoryChooserPM {
    private final ObjectProperty<Path> path = new SimpleObjectProperty<>();
    private DirectoryChooser directoryChooser;
    private JShellService jShellService = JShellService.getInstance();

    public DirectoryChooserPM(){
        directoryChooser = new DirectoryChooser();
    }

    public DirectoryChooser getDirectoryChooser() {
        return directoryChooser;
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
