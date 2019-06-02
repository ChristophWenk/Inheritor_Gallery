package inheritorgallery.view;

import javafx.scene.layout.BorderPane;

// TODO include PMs
public class ApplicationUI extends BorderPane implements ViewMixin {

    private ConsolePane consolePane;
    private InstancePane instancePane;

    public ApplicationUI() {
        init();
    }

    @Override
    public void initializeControls() {
        // Initialize panes
        consolePane = new ConsolePane();
        instancePane = new InstancePane();
    }

    @Override
    public void layoutControls() {
        // Set IDs
        consolePane.setId("consolePane");
        instancePane.setId("instancePane");

        // Layouts

        this.setCenter(consolePane);
        //this.setRight(instancePane);

    }
}
