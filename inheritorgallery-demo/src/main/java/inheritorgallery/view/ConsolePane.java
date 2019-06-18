package inheritorgallery.view;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import jshell.extension.JShellScriptExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This pane contains all UI elements for shell interaction.
 * This includes an input for user commands and an output
 * area for system outputs.
 */
public class ConsolePane extends GridPane implements ViewMixin {

    private static Logger logger = LoggerFactory.getLogger(ConsolePane.class);

    private TextArea jshellOutputTextArea;
    private TextField jshellInputTextField;
    private Button submitButton;

    JShellScriptExecutor jShellScriptExecutor = new JShellScriptExecutor();

    public ConsolePane() {
        init();
        logger.info("Finished initializing ConsolePane");
    }

    @Override
    public void initializeControls() {
        jshellOutputTextArea = new TextArea();
        jshellInputTextField = new TextField();
        submitButton = new Button();
    }

    @Override
    public void layoutControls() {
        // Set IDs
        jshellOutputTextArea.setId("jshellOutputTextArea");
        jshellInputTextField.setId("jshellInputTextField");

        // Layout
        jshellInputTextField.setText("Enter a Java command...");
        jshellOutputTextArea.setEditable(false);
        submitButton.setText("Submit");

        // Add controls
        add(jshellOutputTextArea,1,1,2,1);
        add(jshellInputTextField,1,2,2,1);
        add(submitButton,3,2,1,1);
    }

    @Override
    public void setupEventHandlers() {
        submitButton.setOnAction(event -> {
            String jShellCommand = jShellScriptExecutor.processInput(jshellInputTextField.getText());
            jshellOutputTextArea.appendText(jShellCommand + "\n");
        });
    }
}
