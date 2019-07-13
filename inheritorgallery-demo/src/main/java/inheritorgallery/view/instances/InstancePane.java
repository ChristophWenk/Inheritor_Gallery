package inheritorgallery.view.instances;

import inheritorgallery.view.ViewMixin;
import javafx.beans.value.ChangeListener;
import javafx.scene.layout.FlowPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentationmodel.ColorPM;
import presentationmodel.instance.InstanceStatePM;
import presentationmodel.instance.ObjectPM;

import java.util.ArrayList;

public class InstancePane extends FlowPane implements ViewMixin {

    private static Logger logger = LoggerFactory.getLogger(InstancePane.class);

    private InstanceStatePM instanceStatePM;
    private ColorPM colorPM;
    private ArrayList<ObjectUnit> objectUnits;
    private ChangeListener pmStateListener = (observable, oldValue, newValue) -> this.layoutControls();

    public InstancePane(InstanceStatePM instanceStatePM, ColorPM colorPM){
        this.instanceStatePM = instanceStatePM;
        this.colorPM = colorPM;
        init();

        logger.info("Finished initializing InstancePane");
    }

    @Override
    public void initializeControls() {
        objectUnits = new ArrayList<>();
    }

    @Override
    public void layoutControls() {
        this.getChildren().removeAll(objectUnits);
        objectUnits.clear();

        if (instanceStatePM.getObjectPMs() != null) {
            for (ObjectPM objectPM : instanceStatePM.getObjectPMs()) {
                ObjectUnit objectUnit = new ObjectUnit(objectPM, colorPM);
//                String color = colorPM.getColor(objectPM.getObjectFullName());
//                objectUnit.setStyle("-fx-background-color:" + color);
                objectUnits.add(objectUnit);
            }
        }
        logger.debug("Drawing " + objectUnits.size() + " element(s)...");
        this.getChildren().addAll(objectUnits);
    }

    @Override
    public void setupValueChangedListeners() {
        instanceStatePM.objectPMProperty().addListener(pmStateListener);
    }
}
