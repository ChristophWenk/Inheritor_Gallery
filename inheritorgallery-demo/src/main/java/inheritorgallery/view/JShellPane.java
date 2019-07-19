package inheritorgallery.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import presentationmodel.instance.InstanceStatePM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This pane contains all UI elements for shell interaction.
 * This includes an input for user commands and an output
 * area for system outputs.
 */
public class JShellPane extends BorderPane implements ViewMixin {

    private static Logger logger = LoggerFactory.getLogger(JShellPane.class);

    private InstanceStatePM instanceStatePM;

    private HBox inputElements;

    private TextField jshellInputTextField;
    private ListView<String> commandHistoryList;

    public JShellPane(InstanceStatePM instanceStatePM) {
        this.instanceStatePM = instanceStatePM;
        init();
        logger.info("Finished initializing JShellPane");
    }

    @Override
    public void initializeControls() {
        inputElements = new HBox();
        jshellInputTextField = new TextField();

        commandHistoryList = new ListView<>(instanceStatePM.getCommandHistory());
    }

    @Override
    public void layoutControls() {
        // Layout
        jshellInputTextField.setPromptText("Enter a Java command...");
        jshellInputTextField.setPrefWidth(400);

        inputElements.setPadding(new Insets(10,2,10,2));

        inputElements.getChildren().addAll(jshellInputTextField);

        BorderPane.setMargin(commandHistoryList,new Insets(10,0,0,0));
        BorderPane.setAlignment(commandHistoryList, Pos.CENTER_LEFT);


        this.setCenter(commandHistoryList);
        this.setBottom(inputElements);
    }

    @Override
    public void setupEventHandlers() {
        jshellInputTextField.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                instanceStatePM.setJShellInput((jshellInputTextField.getText()));
                jshellInputTextField.clear();
            }
        });
    }

    @Override
    public void setupBindings() {
        commandHistoryList.setItems(instanceStatePM.getCommandHistory());
    }
}
