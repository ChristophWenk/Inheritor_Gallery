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
    private ArrayList<Line> lines;
    private ArrayList<HBox> hBoxes;
    private VBox vBox;

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
        lines = new ArrayList<>();
        hBoxes = new ArrayList<>();
        vBox = new VBox();
        gridPane = new GridPane();
        pane = new Pane();

        for(ClassPM c : model.getClasses()){
            umlClasses.add(new UmlClass(c));
        }

        for(EdgePM ignored : model.getEdges()){
            lines.add(new Line());
        }
        for (int i = 0; i <= model.getInheritanceDeepness(); i++) {
            hBoxes.add(new HBox());
        }


    }

    @Override
    public void layoutControls() {


//        for (int i=0 ; i < model.getClasses().size(); i++) {
//            gridPane.add(umlClasses.get(i), model.getInheritanceLevelOfClass(model.getClasses().get(i)),i);
//        }

        for (int i=0 ; i < model.getClasses().size(); i++) {
            hBoxes.get(model.getInheritanceLevelOfClass(model.getClasses().get(i)))
                    .getChildren()
                    .add(umlClasses.get(i));
        }
        vBox.getChildren().addAll(hBoxes);
        getChildren().add(vBox);


        //getChildren().add(gridPane);


        Platform.runLater(() -> {

            for (int i=0 ; i < model.getEdges().size(); i++){
                int finalI = i;
                Optional<UmlClass> source = umlClasses.stream().filter(
                        c -> c.getId().equals(model.getEdges().get(finalI).getSource())).findFirst();
                Optional<UmlClass> target = umlClasses.stream().filter(
                        c -> c.getId().equals(model.getEdges().get(finalI).getTarget())).findFirst();


                if (source.isPresent() && target.isPresent()) {
                    //getBoundsInParent() > get x Axis from hBox
                    lines.get(i).setStartX(source.get().getBoundsInParent().getCenterX());
                    //getBoundsInParent() > get y Axis from parent vBox
                    lines.get(i).setStartY(source.get().getParent().getBoundsInParent().getCenterY());

                    lines.get(i).setEndX(target.get().getBoundsInParent().getCenterX());
                    lines.get(i).setEndY(target.get().getParent().getBoundsInParent().getMaxY());

                    pane.getChildren().add(lines.get(i));

                } else {
                    logger.error("Class for Edge missing");
                }
            }
            getChildren().add(pane);

        });

    }

    @Override
    public void setupBindings() {

    }

}
