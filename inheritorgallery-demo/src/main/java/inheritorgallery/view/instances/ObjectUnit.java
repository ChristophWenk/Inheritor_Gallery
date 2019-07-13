package inheritorgallery.view.instances;


import inheritorgallery.view.ViewMixin;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentationmodel.ColorPM;
import presentationmodel.instance.ObjectPM;
import presentationmodel.instance.ReferencePM;
import presentationmodel.uml.ClassPM;

import java.util.ArrayList;
import java.util.List;


public class ObjectUnit extends VBox implements ViewMixin {
    private static Logger logger = LoggerFactory.getLogger(ObjectUnit.class);
    private List<Label> references;

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
        references = new ArrayList<>();

        for (ClassPM part : objectPM.getObjectParts()) {
            ObjectPartUnit objectPartUnit = new ObjectPartUnit(part,colorPM);
            String color = colorPM.getColor(part.getFullClassName());
            objectPartUnit.setStyle("-fx-background-color:" + color);
            objectParts.add(objectPartUnit);
        }

        for (ReferencePM referencePM : objectPM.getReferences()) {
            references.add(new Label(referencePM.getReferenceType() + " " + referencePM.getReferenceName()));
        }
    }

    @Override
    public void layoutControls() {
        if(references != null ) getChildren().addAll(references);
        getChildren().addAll(objectParts);
    }

    @Override
    public void setupBindings() {
    }
}
