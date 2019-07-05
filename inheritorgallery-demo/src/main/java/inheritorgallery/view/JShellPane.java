package inheritorgallery.view;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import presentationmodel.jshell.JShellPM;
import service.jshell.JShellService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This pane contains all UI elements for shell interaction.
 * This includes an input for user commands and an output
 * area for system outputs.
 */
public class JShellPane extends BorderPane implements ViewMixin {

    private static Logger logger = LoggerFactory.getLogger(JShellPane.class);

    private JShellPM jShellPM;

    private HBox inputElements;

    private TextArea jshellOutputTextArea;
    private TextField jshellInputTextField;
    private ListView<String> commandHistoryList;
    private Button submitButton;

    public JShellPane(JShellPM jShellPM) {
        this.jShellPM = jShellPM;
        init();
        logger.info("Finished initializing JShellPane");
    }

    @Override
    public void initializeControls() {
        inputElements = new HBox();
        jshellOutputTextArea = new TextArea();
        jshellInputTextField = new TextField();
        submitButton = new Button();

        commandHistoryList = new ListView<>(jShellPM.getCommandHistory());


    }

    @Override
    public void layoutControls() {
        // Set IDs
        commandHistoryList.setId("jshellOutputTextArea");
        jshellInputTextField.setId("jshellInputTextField");
        submitButton.setId("submitButton");

        // Layout
        commandHistoryList.setItems(jShellPM.getCommandHistory());


        jshellInputTextField.setText("Enter a Java command...");
        commandHistoryList.setEditable(false);
        submitButton.setText("Senden");

        inputElements.setPadding(new Insets(10,10,10,0));
        inputElements.setSpacing(10);

        inputElements.getChildren().addAll(jshellInputTextField,submitButton);

        BorderPane.setMargin(commandHistoryList,new Insets(10,0,0,0));
        BorderPane.setAlignment(commandHistoryList, Pos.CENTER_LEFT);


        this.setCenter(commandHistoryList);
        this.setBottom(inputElements);
    }

    @Override
    public void setupEventHandlers() {
        submitButton.setOnAction(event ->
                jShellPM.setInput(jshellInputTextField.getText()));
    }

    @Override
    public void setupBindings() {
        //commandHistoryList.itemsProperty().bind(jShellPM.getCommandHistory());
//        jShellService.evaluateCode(jshellInputTextField.getText());
//        jshellOutputTextArea.appendText(jShellCommand + "\n");
//

    }
}
