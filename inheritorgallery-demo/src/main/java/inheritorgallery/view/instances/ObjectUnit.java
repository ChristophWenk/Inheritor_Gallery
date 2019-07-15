package inheritorgallery.view.instances;


import inheritorgallery.view.ViewMixin;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
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
            VBox currentClassVBox = new VBox(objectPartUnit);
            currentClassVBox.setPrefWidth(200);

            HBox superClassAndInterfacesHBox = new HBox(currentClassVBox);
//            if(classPM.getImplementedInterfaces() != null)
//                superClassAndInterfacesHBox.getChildren().add(new Label("yay"));

            if (!referencesList.isEmpty()) {
                currentClassVBox.getStyleClass().add("referenceBorder");
            }

            vBoxCurrent.getChildren().add(superClassAndInterfacesHBox);
            vBoxCurrent = currentClassVBox;


            if(classPM.getSuperClass() != null)
            {
                classPM = classPM.getSuperClass();

            }
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
