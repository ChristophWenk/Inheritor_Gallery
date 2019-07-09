package inheritorgallery.view.instances;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import presentationmodel.instance.ObjectPM;

public class ObjectListCell extends ListCell<ObjectPM> {

    @Override
    protected void updateItem(ObjectPM objectPM, boolean empty) {
        super.updateItem(objectPM, empty);

        if(empty || objectPM == null){
            setGraphic(null);
        }
        else {
            Label label = new Label(objectPM.getObjectName());
            setGraphic(label);
        }
    }



}
