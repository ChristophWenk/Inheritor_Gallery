package inheritorgallery.view.instances;

import inheritorgallery.view.ViewMixin;
import javafx.beans.value.ChangeListener;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentationmodel.instance.InstanceStatePM;
import presentationmodel.instance.ObjectPM;

import java.util.ArrayList;

public class InstancePane extends FlowPane implements ViewMixin {

    private static Logger logger = LoggerFactory.getLogger(InstancePane.class);

    private final InstanceStatePM instanceStatePM;
    private HBox hBox;

    private ArrayList<ObjectUnit> objectUnits;

    int i = 0;

    private ChangeListener pmStateListener = (observable, oldValue, newValue) -> {
        System.out.println("==Test " + i + "==");
        this.render();
    };

    public InstancePane(InstanceStatePM instanceStatePM){
        this.instanceStatePM = instanceStatePM;
        init();

        logger.info("Finished initializing InstancePane");
    }

    @Override
    public void initializeControls() {

        objectUnits = new ArrayList<>();
        hBox = new HBox();
    }

    @Override
    public void layoutControls() {

    }

    @Override
    public void setupBindings() {

    }

    @Override
    public void setupValueChangedListeners() {
        instanceStatePM.objectPMProperty().addListener(pmStateListener);
    }

    public void render() {
        this.getChildren().removeAll(objectUnits);
        objectUnits.clear();

        for (ObjectPM objectPM : instanceStatePM.getObjectPMs()) {
            objectUnits.add(new ObjectUnit(objectPM));
        }

        this.getChildren().addAll(objectUnits);
        i++;
    }
}
