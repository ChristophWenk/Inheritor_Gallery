package inheritorgallery.view;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentationmodel.instruction.InstructionPM;

/**
 * View that displays the exercise text
 */
public class InstructionPane extends GridPane implements ViewMixin{

    private static Logger logger = LoggerFactory.getLogger(InstructionPane.class);

    private InstructionPM instructionPM;
    private WebView webView ;
    private ChangeListener pmStateListener = (observable, oldValue, newValue) ->  this.init();

    private TextArea instructionTextArea;

    public InstructionPane(InstructionPM instructionPM) {
        this.instructionPM = instructionPM;
        init();
        logger.info("Finished initializing InstructionPane");
    }

    @Override
    public void initializeControls() {
        instructionTextArea = new TextArea();
        webView = new WebView();
    }

    @Override
    public void layoutControls() {
        // Layout
        webView.getEngine().loadContent(instructionPM.getInstructionText());

        // Add controls
        add(webView,0,0,1,1);
    }

    @Override
    public void setupValueChangedListeners() {
        instructionPM.instructionTextProperty().addListener(pmStateListener);
    }

}
