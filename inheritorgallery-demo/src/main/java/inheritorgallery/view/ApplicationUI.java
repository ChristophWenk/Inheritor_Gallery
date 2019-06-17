package inheritorgallery.view;

import javafx.scene.layout.BorderPane;
import presentationmodel.uml.UmlPM;

// TODO include PMs
public class ApplicationUI extends BorderPane implements ViewMixin {

    private UmlPM model;

    private ConsolePane consolePane;
    private InstancePane instancePane;
    private UmlPane umlPane;

    public ApplicationUI(UmlPM model) {
        this.model = model;
        init();
    }

    @Override
    public void initializeControls() {
        // Initialize panes
        consolePane = new ConsolePane();
        instancePane = new InstancePane();
        umlPane = new UmlPane(model);
    }

    @Override
    public void layoutControls() {
        // Set IDs
        consolePane.setId("consolePane");
        instancePane.setId("instancePane");
        umlPane.setId("umlPane");

        // Layouts
        this.setLeft(consolePane);
        this.setCenter(instancePane);
        this.setRight(umlPane);

    }
}
