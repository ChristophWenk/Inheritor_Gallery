package inheritorgallery.view.instances;


import inheritorgallery.view.ViewMixin;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import presentationmodel.instance.ObjectPM;
import presentationmodel.uml.ClassPM;
import presentationmodel.uml.FieldPM;

import java.util.ArrayList;
import java.util.List;


public class ObjectPartUnit extends VBox implements ViewMixin {
    private List<Label> objectLabels;
    private Label className;
    private ClassPM model;

    public ObjectPartUnit(ClassPM model){
        this.model = model;
        init();
    }

    @Override
    public void initializeControls() {
        className = new Label(model.getName());
        objectLabels = new ArrayList<>();

        for(FieldPM part : model.getFields()){
            objectLabels.add(new Label(part.getName()));
            objectLabels.add(new Label(part.getValue()));
        }

    }

    @Override
    public void layoutControls() {

        getChildren().addAll(className);
        getChildren().addAll(objectLabels);
    }

    @Override
    public void setupBindings() {
        //objectLabel.textProperty().bind(model.objectFullNameProperty());
    }
}
