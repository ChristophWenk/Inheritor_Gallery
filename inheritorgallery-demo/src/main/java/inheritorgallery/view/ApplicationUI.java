package inheritorgallery.view;

import inheritorgallery.view.instances.InstancePane;
import inheritorgallery.view.uml.UMLPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentationmodel.ColorPM;
import presentationmodel.FileChooserPM;
import presentationmodel.instance.InstanceStatePM;
import presentationmodel.instruction.InstructionPM;
import presentationmodel.uml.UmlPM;

/**
 * View that displays and setups all panes needed to run the application
 */
public class ApplicationUI extends SplitPane implements ViewMixin {

    private static Logger logger = LoggerFactory.getLogger(ApplicationUI.class);

    private UmlPM umlPM;
    private InstanceStatePM instanceStatePM;
    private InstructionPM instructionPM;
    private FileChooserPM fileChooserPM;
    private ColorPM colorPM;
    private Stage primaryStage;

    private LeftPane leftPane;
    private InstancePane instancePane;
    private UMLPane umlPane;

    public ApplicationUI(Stage primaryStage, FileChooserPM fileChooserPM, InstanceStatePM instanceStatePM, UmlPM umlPM,
                         InstructionPM instructionPM, ColorPM colorPM) {
        this.primaryStage = primaryStage;
        this.umlPM = umlPM;
        this.instanceStatePM = instanceStatePM;
        this.instructionPM = instructionPM;
        this.colorPM = colorPM;
        this.fileChooserPM = fileChooserPM;
        init();
        logger.info("Finished initializing ApplicationUI");
    }

    @Override
    public void initializeControls() {
        // Initialize panes
        leftPane = new LeftPane(primaryStage, fileChooserPM, instanceStatePM, instructionPM);
        instancePane = new InstancePane(instanceStatePM, colorPM);
        umlPane = new UMLPane(umlPM, colorPM);
    }

    @Override
    public void layoutControls() {
        ScrollPane umlScrollPane = new ScrollPane(umlPane);
        umlScrollPane.setPannable(true);

        ScrollPane instanceScrollPane = new ScrollPane(instancePane);
        instanceScrollPane.setFitToWidth(true);


        getItems().addAll(
                leftPane,
                new SplitPane(instanceScrollPane,umlScrollPane)
        );
        setDividerPosition(0,0.25);
    }

    @Override
    public void setupEventHandlers() {
        instancePane.setupValueChangedListeners();
    }
}
