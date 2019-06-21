package inheritorgallery.view;

import inheritorgallery.view.util.UmlClass;
import javafx.scene.layout.GridPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentationmodel.uml.UmlPM;

import java.util.ArrayList;

public class UmlPane extends GridPane implements ViewMixin{

    private static Logger logger = LoggerFactory.getLogger(UmlPane.class);

    private final UmlPM model;

    private ArrayList<UmlClass> umlClasses = new ArrayList<>();


    public UmlPane(UmlPM model) {
        this.model = model;
        init();
        logger.info("Finished initializing UmlPane");
    }

    @Override
    public void initializeControls() {

        for (int i=0 ; i < model.getClasses().size(); i++) {
            umlClasses.add(new UmlClass(model.getClasses().get(i)));
        }

    }

    @Override
    public void layoutControls() {
        for (int i=0 ; i < model.getClasses().size(); i++) {
            add(umlClasses.get(i), 1,i);
        }
    }

    @Override
    public void setupBindings() {

    }

}
