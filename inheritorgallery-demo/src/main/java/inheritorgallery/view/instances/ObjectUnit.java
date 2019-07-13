package inheritorgallery.view.instances;


import inheritorgallery.view.ViewMixin;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentationmodel.ColorPM;
import presentationmodel.instance.ObjectPM;
import presentationmodel.uml.ClassPM;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class ObjectUnit extends GridPane implements ViewMixin {
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

        for (ClassPM classPM : objectPM.getObjectParts()) {
            ObjectPartUnit objectPartUnit = null;

            List<String> referencesList = objectPM.getReferences().stream()
                    .filter(referenceType -> referenceType.getReferenceType().equals(classPM.getName()))
                    .map(referencePM -> referencePM.getReferenceName())
                    .collect(Collectors.toList());
            if (!referencesList.isEmpty()) {
                objectPartUnit = new ObjectPartUnit(classPM, colorPM, objectPM, referencesList);
                objectPartUnit.getStyleClass().add("referencedObjectPartUnit");
            }
            else {
                objectPartUnit = new ObjectPartUnit(classPM, colorPM, objectPM, null);
                objectPartUnit.getStyleClass().add("classBox");
            }

            String color = colorPM.getColor(classPM.getFullClassName());
            objectPartUnit.setStyle("-fx-background-color:" + color);

            objectParts.add(objectPartUnit);
        }
    }

    @Override
    public void layoutControls() {
        int columnIndex = 0;
        int rowIndex = 0;
        add(objectParts.get(0), columnIndex++,rowIndex++,objectPM.getObjectWidth(),1);

        if(objectParts.size() > 1){
            for(rowIndex = 1; rowIndex < objectPM.getObjectParts().size(); rowIndex++ ){
                if(!objectPM.getObjectParts().get(rowIndex).isIsInterface())
                    add(objectParts.get(rowIndex), 0,rowIndex);
                else
                    //todo: adjust rowspan to smaller number
                    add(objectParts.get(rowIndex), columnIndex++,1,1,100);
            }
        }

    }

    @Override
    public void setupBindings() {
    }
}
