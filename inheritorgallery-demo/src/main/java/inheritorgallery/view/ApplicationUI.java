package inheritorgallery.view;

import inheritorgallery.view.instances.InstancePane;
import inheritorgallery.view.uml.UmlPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentationmodel.instance.InstanceStatePM;
import presentationmodel.instruction.InstructionPM;
import presentationmodel.uml.UmlPM;

public class ApplicationUI extends BorderPane implements ViewMixin {

    private static Logger logger = LoggerFactory.getLogger(ApplicationUI.class);

    private UmlPM umlPM;
    private InstanceStatePM instanceStatePM;
    private InstructionPM instructionPM;

    private LeftPane leftPane;
    private InstancePane instancePane;
    private UmlPane umlPane;

    public ApplicationUI(InstanceStatePM instanceStatePM, UmlPM umlPM, InstructionPM instructionPM) {
        this.umlPM = umlPM;
        this.instanceStatePM = instanceStatePM;
        this.instructionPM = instructionPM;
        init();
        logger.info("Finished initializing ApplicationUI");
    }

    @Override
    public void initializeControls() {
        // Initialize panes
        leftPane = new LeftPane(instanceStatePM, instructionPM);
        instancePane = new InstancePane(instanceStatePM);
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

    @Override
    public void setupEventHandlers() {
        instancePane.setupValueChangedListeners();
    }
}
