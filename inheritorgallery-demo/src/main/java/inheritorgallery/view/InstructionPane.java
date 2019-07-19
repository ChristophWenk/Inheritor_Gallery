package inheritorgallery.view;

import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentationmodel.instruction.InstructionPM;

public class InstructionPane extends GridPane implements ViewMixin{

    private static Logger logger = LoggerFactory.getLogger(InstructionPane.class);

    private InstructionPM instructionPM;
    private WebView webView ;

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
        instructionTextArea.setPrefWidth(200);

        // Layout
        webView.getEngine().loadContent(instructionPM.getInstructionText());

        // Add controls
        add(webView,0,0,1,1);
    }
}
