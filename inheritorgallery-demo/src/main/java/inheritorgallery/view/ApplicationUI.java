package inheritorgallery.view;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentationmodel.jshell.JShellPM;
import presentationmodel.uml.UmlPM;

public class ApplicationUI extends BorderPane implements ViewMixin {

    private static Logger logger = LoggerFactory.getLogger(ApplicationUI.class);

    private UmlPM umlPM;
    private JShellPM jShellPM;

    private LeftPane leftPane;
    private InstancePane instancePane;
    private UmlPane umlPane;

    public ApplicationUI(JShellPM jShellPM, UmlPM umlPM) {
        this.umlPM = umlPM;
        this.jShellPM = jShellPM;
        init();
        logger.info("Finished initializing ApplicationUI");
    }

    @Override
    public void initializeControls() {
        // Initialize panes
        leftPane = new LeftPane(jShellPM);
        instancePane = new InstancePane();
        umlPane = new UmlPane(umlPM);
    }

    @Override
    public void layoutControls() {
        // Set IDs
        leftPane.setId("leftPane");
        instancePane.setId("instancePane");
        umlPane.setId("umlPane");

        ScrollPane umlScrollPane = new ScrollPane(umlPane);
        umlScrollPane.setPannable(true);
        umlScrollPane.setPrefWidth(600);

        // Layouts
        this.setLeft(leftPane);
        this.setCenter(instancePane);
        this.setRight(umlScrollPane);

    }
}
