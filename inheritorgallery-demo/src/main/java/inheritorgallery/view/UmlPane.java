package inheritorgallery.view;

import inheritorgallery.view.util.UmlClass;
import javafx.application.Platform;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentationmodel.uml.ClassPM;
import presentationmodel.uml.EdgePM;
import presentationmodel.uml.UmlPM;

import java.util.ArrayList;
import java.util.Optional;

public class UmlPane extends StackPane implements ViewMixin{

    private static Logger logger = LoggerFactory.getLogger(UmlPane.class);

    private final UmlPM model;

    private ArrayList<UmlClass> umlClasses;
    private ArrayList<EdgePM> edges;

    private Line line;

    private GridPane gridPane;
    private Pane pane;



    public UmlPane(UmlPM model) {
        this.model = model;
        init();
        logger.info("Finished initializing UmlPane");
    }

    @Override
    public void initializeControls() {
        umlClasses = new ArrayList<>();
        edges = new ArrayList<>();
        gridPane = new GridPane();
        pane = new Pane();

        line = new Line();


        for(ClassPM c : model.getClasses()){
            umlClasses.add(new UmlClass(c));
        }

    }

    @Override
    public void layoutControls() {


        for (int i=0 ; i < model.getClasses().size(); i++) {
            gridPane.add(umlClasses.get(i), 1,i);
        }

        getChildren().add(gridPane);


        Platform.runLater(() -> {

            Optional<UmlClass> source = umlClasses.stream().filter(c->c.getId().equals("Buyable")).findFirst();
            Optional<UmlClass> target = umlClasses.stream().filter(c->c.getId().equals("AntiqueBuyableFahrrad")).findFirst();

            if(source.isPresent() && target.isPresent()){
                line.setStartX(umlClasses.get(1).getBoundsInParent().getCenterX());
                line.setStartY(umlClasses.get(1).getBoundsInParent().getMaxY());

                line.setEndX(umlClasses.get(3).getBoundsInParent().getCenterX());
                line.setEndY(umlClasses.get(3).getBoundsInParent().getMinY());

                pane.getChildren().add(line);
                getChildren().add(pane);
            }
            else {
                logger.error("Class for Edge missing");
            }

        });

    }

    @Override
    public void setupBindings() {

    }

}
