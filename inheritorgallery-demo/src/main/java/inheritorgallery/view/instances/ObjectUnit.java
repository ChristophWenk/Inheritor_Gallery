package inheritorgallery.view.instances;


import inheritorgallery.view.ViewMixin;
import javafx.scene.layout.VBox;
import presentationmodel.ColorPM;
import presentationmodel.instance.ObjectPM;
import presentationmodel.uml.ClassPM;

import java.util.ArrayList;
import java.util.List;


public class ObjectUnit extends VBox implements ViewMixin {
    private List<ObjectPartUnit> objectParts;
    private ObjectPM objectPM;
    private ColorPM colorPM;

    public ObjectUnit(ObjectPM objectPM, ColorPM colorPM){
        this.objectPM = objectPM;
        this.colorPM = colorPM;
        init();
    }

    @Override
    public void initializeControls() {
        objectParts = new ArrayList<>();

        for(ClassPM part : objectPM.getObjectParts()) {
            ObjectPartUnit objectPartUnit = new ObjectPartUnit(part);
            String color = colorPM.getColor(part.getFullClassName());
            objectPartUnit.setStyle("-fx-background-color:" + color);
            objectParts.add(objectPartUnit);
        }
    }

    @Override
    public void layoutControls() {

        getChildren().addAll(objectParts);
    }

    @Override
    public void setupBindings() {
    }
}
