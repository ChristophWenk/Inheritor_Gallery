package inheritorgallery.view;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import presentationmodel.uml.UmlPM;

public class UmlPane extends GridPane implements ViewMixin{

    private final UmlPM model;

    private Label classNameLabel;

    public UmlPane(UmlPM model) {
        this.model = model;
        init();
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
