package inheritorgallery.view;

import inheritorgallery.view.instances.InstancePane;
import inheritorgallery.view.uml.UmlPane;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentationmodel.ColorPM;
import presentationmodel.instance.InstanceStatePM;
import presentationmodel.instruction.InstructionPM;
import presentationmodel.uml.UmlPM;

public class ApplicationUI extends BorderPane implements ViewMixin {

    private static Logger logger = LoggerFactory.getLogger(ApplicationUI.class);

    private UmlPM umlPM;
    private InstanceStatePM instanceStatePM;
    private InstructionPM instructionPM;
    private ColorPM colorPM;
    private DirectoryChooser directoryChooser;
    private Stage primaryStage;

    private LeftPane leftPane;
    private InstancePane instancePane;
    private UmlPane umlPane;

    public ApplicationUI(Stage primaryStage, DirectoryChooser directoryChooser,InstanceStatePM instanceStatePM, UmlPM umlPM,
                         InstructionPM instructionPM, ColorPM colorPM) {
        this.primaryStage = primaryStage;
        this.umlPM = umlPM;
        this.instanceStatePM = instanceStatePM;
        this.instructionPM = instructionPM;
        this.colorPM = colorPM;
        this.directoryChooser = directoryChooser;
        init();
        logger.info("Finished initializing ApplicationUI");
    }

    @Override
    public void initializeControls() {
        // Initialize panes
        leftPane = new LeftPane(primaryStage,directoryChooser, instanceStatePM, instructionPM);
        leftPane.setMaxWidth(300);
        instancePane = new InstancePane(instanceStatePM, colorPM);
        umlPane = new UmlPane(umlPM, colorPM);
        umlPane.setMaxWidth(300);
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
