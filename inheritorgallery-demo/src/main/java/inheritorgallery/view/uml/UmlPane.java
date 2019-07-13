package inheritorgallery.view.uml;

import inheritorgallery.view.ViewMixin;
import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentationmodel.uml.ClassPM;
import presentationmodel.uml.EdgePM;
import presentationmodel.uml.UmlPM;

import java.util.ArrayList;
import java.util.Optional;


public class UmlPane extends StackPane implements ViewMixin {

    private static Logger logger = LoggerFactory.getLogger(UmlPane.class);

    private final UmlPM model;

    private ArrayList<UmlClass> umlClasses;
    private ArrayList<Line> lines;
    private ArrayList<Polygon> triangles;
    private ArrayList<HBox> hBoxes;
    private VBox vBox;
    private Pane linePane;
    private ScrollPane scrollPane;



    public UmlPane(UmlPM model) {
        this.model = model;
        init();
        logger.info("Finished initializing UmlPane");
    }

    @Override
    public void initializeControls() {
        umlClasses = new ArrayList<>();
        lines = new ArrayList<>();
        triangles = new ArrayList<>();
        hBoxes = new ArrayList<>();
        vBox = new VBox(40);
        linePane = new Pane();
        scrollPane = new ScrollPane();

        for(ClassPM c : model.getClasses()){
            umlClasses.add(new UmlClass(c));
        }

        for(EdgePM ignored : model.getEdges()){
            lines.add(new Line());
            triangles.add(new Polygon());
        }
        for (int i = 0; i <= model.getInheritanceDeepness(); i++) {
            hBoxes.add(new HBox(20));
        }


    }

    @Override
    public void layoutControls() {

        for (int i=0 ; i < model.getClasses().size(); i++) {
            hBoxes.get(model.getClasses().get(i).getInheritanceLevel())
                    .getChildren()
                    .add(umlClasses.get(i));
        }
        vBox.getChildren().addAll(hBoxes);

        getChildren().addAll(vBox);

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
                    lines.get(i).setStartY(source.get().getParent().getBoundsInParent().getMinY());

                    lines.get(i).setEndX(target.get().getBoundsInParent().getCenterX());
                    lines.get(i).setEndY(target.get().getParent().getBoundsInParent().getMaxY());

                    if(model.getEdges().get(i).getType().equals("extends")){
                        lines.get(i).getStyleClass().add("extendsLine");
                    } else if(model.getEdges().get(i).getType().equals("implements")){
                        lines.get(i).getStyleClass().add("implementsLine");
                    }

                    //add arrow head to line
                    triangles.get(i).getPoints().addAll(
                            getArrowHeadForLine(lines.get(i))
                    );

                    linePane.getChildren().add(lines.get(i));
                    linePane.getChildren().add(triangles.get(i));


                } else {
                    logger.error("Class for Edge missing");
                }
            }
            getChildren().add(linePane);
        });

    }

    @Override
    public void setupBindings() {

    }


    private Double[] getArrowHeadForLine(Line line){
        //adapted from https://stackoverflow.com/questions/41353685/how-to-draw-arrow-javafx-pane

        double ex = line.getEndX();
        double ey = line.getEndY();
        double sx = line.getStartX();
        double sy = line.getStartY();

        double factor = 20 / Math.hypot(sx-ex, sy-ey);
        double factorO = 10 / Math.hypot(sx-ex, sy-ey);

        // part in direction of main line
        double dx = (sx - ex) * factor;
        double dy = (sy - ey) * factor;

        // part ortogonal to main line
        double ox = (sx - ex) * factorO;
        double oy = (sy - ey) * factorO;

        Double[] arr = { ex, ey,  ex + dx - oy,ey + dy + ox ,  ex + dx + oy,ey + dy - ox};

        return arr;
    }

}
