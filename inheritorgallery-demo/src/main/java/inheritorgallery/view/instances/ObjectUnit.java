package inheritorgallery.view.instances;


import inheritorgallery.view.ViewMixin;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentationmodel.ColorPM;
import presentationmodel.instance.ObjectPM;
import presentationmodel.instance.ReferencePM;
import presentationmodel.uml.ClassPM;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class ObjectUnit extends VBox implements ViewMixin {
    private static Logger logger = LoggerFactory.getLogger(ObjectUnit.class);
    private int partWidth = 150;

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
        List<ClassPM> implementedInterfaces = new ArrayList<>();

        ClassPM classPM = objectPM.getObjectTree();

        while(true) {
            ObjectPartUnit objectPartUnit = null;
            String currentSimpleClassName = classPM.getName();

            List<ReferencePM> referencesList = objectPM.getReferences().stream()
                    .filter(referenceType -> referenceType.getReferenceType().equals(currentSimpleClassName))
                    .collect(Collectors.toList());

            objectPartUnit = new ObjectPartUnit(classPM, colorPM, referencesList);
            Pane refBorderClass = new Pane();

            VBox currentClassVBox = new VBox(objectPartUnit);
            StackPane objectPartUnitStackPane = new StackPane(currentClassVBox,refBorderClass);

            currentClassVBox.setMinWidth(partWidth);

            if (!referencesList.isEmpty()) {
                refBorderClass.getStyleClass().add("referenceBorder");
            }

            HBox superClassAndInterfacesHBox = new HBox(objectPartUnitStackPane);

            // add interfaces if present
            if(implementedInterfaces.size() > 0){
                List<StackPane> interfaceVBoxList = new ArrayList<>();

                for (int i = 0; implementedInterfaces.size() > i; i++){
                    String currentSimpleClassNameOfInterface  = implementedInterfaces.get(i).getName();
                    referencesList = objectPM.getReferences().stream()
                            .filter(referenceType -> referenceType.getReferenceType().equals(currentSimpleClassNameOfInterface))
                            .collect(Collectors.toList());
                    objectPartUnit = new ObjectPartUnit(implementedInterfaces.get(i), colorPM, referencesList);

                    VBox currentInterfaceVBox = new VBox(objectPartUnit);
                    Pane refBorderInterface= new Pane();

                    StackPane currentInterfaceStackPane = new StackPane(currentInterfaceVBox,refBorderInterface);

                    currentInterfaceVBox.setMinWidth(partWidth);
                    if (!referencesList.isEmpty()) {
                       refBorderInterface.getStyleClass().add("referenceBorder");
                    }
                    interfaceVBoxList.add(currentInterfaceStackPane);
                }

                superClassAndInterfacesHBox.getChildren().addAll(interfaceVBoxList);

            }


            vBoxCurrent.getChildren().add(superClassAndInterfacesHBox);
            vBoxCurrent = currentClassVBox;


            if(classPM.getSuperClass() != null)
            {
                implementedInterfaces = classPM.getImplementedInterfaces();
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
