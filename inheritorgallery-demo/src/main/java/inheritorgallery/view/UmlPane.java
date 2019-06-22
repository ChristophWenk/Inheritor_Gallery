package inheritorgallery.view;

import inheritorgallery.view.util.UmlClass;
import javafx.application.Platform;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentationmodel.uml.UmlPM;

import java.util.ArrayList;

public class UmlPane extends StackPane implements ViewMixin{

    private static Logger logger = LoggerFactory.getLogger(UmlPane.class);

    private final UmlPM model;

    private ArrayList<UmlClass> umlClasses = new ArrayList<>();

    private Line line, line2;

    private GridPane gridPane;
    private Pane pane;



    public UmlPane(UmlPM model) {
        this.model = model;
        init();
        logger.info("Finished initializing UmlPane");
    }

    @Override
    public void initializeControls() {
        gridPane = new GridPane();
        pane = new Pane();

        line = new Line();
        line2 = new Line();

        for (int i=0 ; i < model.getClasses().size(); i++) {
            umlClasses.add(new UmlClass(model.getClasses().get(i)));
        }
    }

    @Override
    public void layoutControls() {


        for (int i=0 ; i < model.getClasses().size(); i++) {
            gridPane.add(umlClasses.get(i), 1,i);
        }

        getChildren().add(gridPane);


        Platform.runLater(() -> {

            line.setStartX(umlClasses.get(1).getBoundsInParent().getCenterX());
            line.setStartY(umlClasses.get(1).getBoundsInParent().getMaxY());

            line.setEndX(umlClasses.get(3).getBoundsInParent().getCenterX());
            line.setEndY(umlClasses.get(3).getBoundsInParent().getMinY());

            pane.getChildren().addAll(line, line2);
            getChildren().add(pane);
        });

    }

    @Override
    public void setupBindings() {

    }

}
