package inheritorgallery.view;

import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentationmodel.uml.UmlPM;

// TODO include PMs
public class ApplicationUI extends BorderPane implements ViewMixin {

    private static Logger logger = LoggerFactory.getLogger(ApplicationUI.class);

    private UmlPM model;

    private InteractionPane interactionPane;
    //private ConsolePane consolePane;
    private InstancePane instancePane;
    private UmlPane umlPane;

    public ApplicationUI(UmlPM model) {
        this.model = model;
        init();
        logger.info("Finished initializing ApplicationUI");
    }

    @Override
    public void initializeControls() {
        // Initialize panes
        interactionPane = new InteractionPane();
        //consolePane = new ConsolePane();
        instancePane = new InstancePane();
        umlPane = new UmlPane(model);
    }

    @Override
    public void layoutControls() {
        // Set IDs
        interactionPane.setId("interactionPane");
        //consolePane.setId("consolePane");
        instancePane.setId("instancePane");
        umlPane.setId("umlPane");

        // Layouts
        //this.setLeft(consolePane);
        this.setLeft(interactionPane);
        this.setCenter(instancePane);
        this.setRight(umlPane);

    }
}
