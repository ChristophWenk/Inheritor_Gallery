package inheritorgallery.view;

import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentationmodel.instruction.InstructionPM;
import service.instruction.AsciiDocService;

public class InstructionPane extends GridPane implements ViewMixin{

    private static Logger logger = LoggerFactory.getLogger(InstructionPane.class);

    private InstructionPM model;
    private WebView webView ;

    private TextArea instructionTextArea;

    public InstructionPane() {
        // TODO Evaluate if model is really needed here
        AsciiDocService asciiDocService = new AsciiDocService();
        String instructionTextHTML = asciiDocService.convertFile("/instructions/instructions.adoc");
        model = new InstructionPM(instructionTextHTML);

        webView = new WebView();
        webView.getEngine().loadContent(model.getInstructionText());

        init();
        logger.info("Finished initializing InstructionPane");
    }

    @Override
    public void initializeControls() {
        instructionTextArea = new TextArea();
    }

    @Override
    public void layoutControls() {
        // Set IDs
        instructionTextArea.setId("instructionTextArea");
        webView.setId("browser");
        // Add content
        //instructionTextArea.setText();

        // Add controls
       // this.getChildren().add(webView);
        add(webView,0,0,1,1);
    }
}
