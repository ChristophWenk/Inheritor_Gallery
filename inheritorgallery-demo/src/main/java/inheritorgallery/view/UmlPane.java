package inheritorgallery.view;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentationmodel.uml.UmlPM;

public class UmlPane extends GridPane implements ViewMixin{

    private static Logger logger = LoggerFactory.getLogger(UmlPane.class);

    private final UmlPM model;

    private Label classNameLabel;

    public UmlPane(UmlPM model) {
        this.model = model;
        init();
        logger.info("Finished initializing UmlPane");
    }

    @Override
    public void initializeControls() {
        classNameLabel = new Label();
    }

    @Override
    public void layoutControls() {
        add(classNameLabel,1,1);
    }

    @Override
    public void setupBindings() {
        classNameLabel.textProperty().bind(model.getClasses().get(0).nameProperty());
    }

}
