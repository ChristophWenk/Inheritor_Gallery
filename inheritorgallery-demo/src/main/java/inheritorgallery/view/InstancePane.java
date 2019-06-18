package inheritorgallery.view;

import javafx.scene.layout.GridPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InstancePane extends GridPane implements ViewMixin {

    private static Logger logger = LoggerFactory.getLogger(InstancePane.class);

    @Override
    public void initializeControls() {
        init();
        logger.info("Finished initializing InstancePane");
    }

    @Override
    public void layoutControls() {

    }
}
