package inheritorgallery.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import service.JShellService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This pane contains all UI elements for shell interaction.
 * This includes an input for user commands and an output
 * area for system outputs.
 */
public class ConsolePane extends BorderPane implements ViewMixin {

    private static Logger logger = LoggerFactory.getLogger(ConsolePane.class);

    private HBox inputElements;

    private TextArea jshellOutputTextArea;
    private TextField jshellInputTextField;
    private Button submitButton;

    JShellService jShellService = new JShellService();

    public ConsolePane() {
        init();
        logger.info("Finished initializing ConsolePane");
    }

    @Override
    public void initializeControls() {
        inputElements = new HBox();
        jshellOutputTextArea = new TextArea();
        jshellInputTextField = new TextField();
        submitButton = new Button();
    }

    @Override
    public void layoutControls() {
        // Set IDs
        jshellOutputTextArea.setId("jshellOutputTextArea");
        jshellInputTextField.setId("jshellInputTextField");
        submitButton.setId("submitButton");

        // Layout
        jshellInputTextField.setText("Enter a Java command...");
        jshellOutputTextArea.setEditable(false);
        submitButton.setText("Senden");

        inputElements.setPadding(new Insets(10,10,10,0));
        inputElements.setSpacing(10);

        inputElements.getChildren().addAll(jshellInputTextField,submitButton);

        BorderPane.setMargin(jshellOutputTextArea,new Insets(10,0,0,0));
        BorderPane.setAlignment(jshellOutputTextArea, Pos.CENTER_LEFT);

        this.setCenter(jshellOutputTextArea);
        this.setBottom(inputElements);
    }

    @Override
    public void setupEventHandlers() {
        submitButton.setOnAction(event -> {
            String jShellCommand = jShellService.processInput(jshellInputTextField.getText());
            jshellOutputTextArea.appendText(jShellCommand + "\n");
        });
    }
}
