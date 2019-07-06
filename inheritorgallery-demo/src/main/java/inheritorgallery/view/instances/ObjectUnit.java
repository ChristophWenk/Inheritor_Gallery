package inheritorgallery.view.instances;


import inheritorgallery.view.ViewMixin;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import presentationmodel.instance.ObjectPM;


public class ObjectUnit extends VBox implements ViewMixin {
    private Label objectLabel;
    private ObjectPM model;

    public ObjectUnit(ObjectPM model){
        this.model = model;
        init();
    }

    @Override
    public void initializeControls() {
        objectLabel = new Label();
    }

    @Override
    public void layoutControls() {
        getChildren().addAll(objectLabel);
    }

    @Override
    public void setupBindings() {
        objectLabel.textProperty().bind(model.objectNameProperty());
    }
}
