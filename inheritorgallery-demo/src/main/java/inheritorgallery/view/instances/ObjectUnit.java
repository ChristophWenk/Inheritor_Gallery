package inheritorgallery.view.instances;


import inheritorgallery.view.ViewMixin;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import presentationmodel.instance.ObjectPM;
import presentationmodel.uml.ClassPM;

import java.util.ArrayList;
import java.util.List;


public class ObjectUnit extends VBox implements ViewMixin {
    //private List<Label> objectLabels;
    private List<ObjectPartUnit> objectParts;
    private ObjectPM model;

    public ObjectUnit(ObjectPM model){
        this.model = model;
        init();
    }

    @Override
    public void initializeControls() {
        //objectLabels = new ArrayList<>();
        objectParts = new ArrayList<>();

        for(ClassPM part : model.getObjectParts())
            objectParts.add(new ObjectPartUnit(part));
            //objectLabels.add(new Label(part.getFullClassName()));
    }

    @Override
    public void layoutControls() {

        getChildren().addAll(objectParts);
    }

    @Override
    public void setupBindings() {
        //objectLabel.textProperty().bind(model.objectFullNameProperty());
    }
}
