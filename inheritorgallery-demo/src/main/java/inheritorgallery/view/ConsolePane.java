package inheritorgallery.view;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import jshell.extension.JShellScriptExecutor;

public class ConsolePane extends GridPane implements ViewMixin {

    private TextArea jshellOutputTextArea;
    private TextField jshellInputTextField;
    private Button submitButton;

    JShellScriptExecutor jShellScriptExecutor = new JShellScriptExecutor();

    public ConsolePane() {
        init();
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
        submitButton.setText("Submit");

        // Add controls
        add(jshellOutputTextArea,1,1,2,1);
        add(jshellInputTextField,1,2,2,1);
        add(submitButton,3,2,1,1);
    }

    @Override
    public void setupEventHandlers() {
        submitButton.setOnAction(event -> {
            jShellScriptExecutor.acceptInput(jshellInputTextField.getText());
        });
    }
}
