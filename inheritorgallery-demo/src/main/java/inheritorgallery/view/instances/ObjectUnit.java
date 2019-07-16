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
import java.util.stream.Collectors;


public class ObjectUnit extends VBox implements ViewMixin {
    private static Logger logger = LoggerFactory.getLogger(ObjectUnit.class);

    private ObjectPM objectPM;
    private ColorPM colorPM;
    private VBox vBoxRoot;


    public ObjectUnit(ObjectPM objectPM, ColorPM colorPM){
        this.objectPM = objectPM;
        this.colorPM = colorPM;
        init();
    }

    @Override
    public void initializeControls() {
        vBoxRoot = new VBox();
        VBox vBoxCurrent = vBoxRoot;

        ClassPM classPM = objectPM.getObjectTree();

        while(true) {
            ObjectPartUnit objectPartUnit = null;
            String currentSimpleClassName = classPM.getName();

            List<ReferencePM> referencesList = objectPM.getReferences().stream()
                    .filter(referenceType -> referenceType.getReferenceType().equals(currentSimpleClassName))
                    .collect(Collectors.toList());

            objectPartUnit = new ObjectPartUnit(classPM, colorPM, referencesList);
            VBox vBoxToAdd = new VBox(objectPartUnit);

            if (!referencesList.isEmpty()) {
                vBoxToAdd.getStyleClass().add("referenceBorder");
            }

            vBoxCurrent.getChildren().add(vBoxToAdd);

            vBoxCurrent = vBoxToAdd;

            if(classPM.hasSuperClass())  classPM = classPM.getSuperClass();
            else break;
        }
    }

    @Override
    public void layoutControls() {
        getChildren().addAll(vBoxRoot);

    }

    @Override
    public void setupBindings() {
    }
}
