package inheritorgallery.view.instances;

import inheritorgallery.view.ViewMixin;
import javafx.scene.layout.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentationmodel.instance.InstanceStatePM;
import presentationmodel.instance.ObjectPM;

import java.util.ArrayList;

public class InstancePane extends StackPane implements ViewMixin {

    private static Logger logger = LoggerFactory.getLogger(InstancePane.class);

    private final InstanceStatePM model;
    private HBox hBox;

    private ArrayList<ObjectUnit> objectUnits;

    public InstancePane(InstanceStatePM model){
        this.model = model;
        init();
    }

    @Override
    public void initializeControls() {
        objectUnits = new ArrayList<>();
        hBox = new HBox();

        for(ObjectPM objectPM : model.getObjectPMs()){
            objectUnits.add(new ObjectUnit(objectPM));
        }

        logger.info("Finished initializing InstancePane");
    }

    @Override
    public void layoutControls() {
        //for (int i=0 ; i < model.getObjectPMs().size(); i++) {


        //}

        hBox.getChildren().addAll(objectUnits);

    }
}
