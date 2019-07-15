package inheritorgallery.view.instances;


import inheritorgallery.view.ViewMixin;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentationmodel.ColorPM;
import presentationmodel.instance.ObjectPM;
import presentationmodel.instance.ReferencePM;
import presentationmodel.uml.ClassPM;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class ObjectUnit extends VBox implements ViewMixin {
    private static Logger logger = LoggerFactory.getLogger(ObjectUnit.class);

    private List<ObjectPartUnit> objectParts;
    private ObjectPM objectPM;
    private ColorPM colorPM;
    private VBox vBox;


    public ObjectUnit(ObjectPM objectPM, ColorPM colorPM){
        this.objectPM = objectPM;
        this.colorPM = colorPM;
        init();
    }

    @Override
    public void initializeControls() {
        objectParts = new ArrayList<>();

        ClassPM classPM = objectPM.getObjectTree();

        while(true) {
            ObjectPartUnit objectPartUnit = null;
            String currentSimpleClassName = classPM.getName();

            List<ReferencePM> referencesList = objectPM.getReferences().stream()
                    .filter(referenceType -> referenceType.getReferenceType().equals(currentSimpleClassName))
                    .collect(Collectors.toList());

            objectPartUnit = new ObjectPartUnit(classPM, colorPM, referencesList);

            if (!referencesList.isEmpty()) {
                objectPartUnit.getStyleClass().add("referenceBorder");
            }

            objectParts.add(objectPartUnit);



            vBox = new VBox(new Label("outer"));
            VBox vBoxInner = new VBox(new Label("inner"));

            vBox.getChildren().add(vBoxInner);

            vBox.getStyleClass().add("referenceBorder");

            vBoxInner.getStyleClass().add("referenceBorder");


            if(classPM.hasSuperClass())  classPM = classPM.getSuperClass();
            else break;
        }
    }

    @Override
    public void layoutControls() {
        getChildren().addAll(objectParts);

        getChildren().addAll(vBox);

    }

    @Override
    public void setupBindings() {
    }
}
