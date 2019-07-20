package inheritorgallery.view.uml;

import inheritorgallery.view.ViewMixin;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import presentationmodel.ColorPM;
import presentationmodel.uml.ClassPM;
import presentationmodel.uml.EdgePM;
import presentationmodel.uml.UmlPM;

import java.util.ArrayList;
import java.util.Optional;


public class UmlPane extends StackPane implements ViewMixin {

    private static Logger logger = LoggerFactory.getLogger(UmlPane.class);

    private final UmlPM umlPM;
    private ColorPM colorPM;
    private ChangeListener pmStateListener = (observable, oldValue, newValue) -> this.init();


    private ArrayList<UmlClassPane> umlClassPanes;
    private ArrayList<Line> lines;
    private ArrayList<Polygon> arrowHeadList;
    private ArrayList<HBox> inheritanceLevelHBox;
    private VBox vBox;
    private Pane linePane;

    public UmlPane(UmlPM umlPM, ColorPM colorPM) {
        this.umlPM = umlPM;
        this.colorPM = colorPM;
        init();
        logger.info("Finished initializing UmlPane");
    }

    @Override
    public void initializeControls() {
        umlClassPanes = new ArrayList<>();
        lines = new ArrayList<>();
        arrowHeadList = new ArrayList<>();
        inheritanceLevelHBox = new ArrayList<>();
        vBox = new VBox(40);
        linePane = new Pane();


        for(ClassPM classPM : umlPM.getClassesObject()){
            UmlClassPane umlClassPane = new UmlClassPane(classPM);
            String color = colorPM.getColor(classPM.getFullClassName());
            umlClassPane.setStyle("-fx-background-color:" + color);
            umlClassPanes.add(umlClassPane);
        }

        for(EdgePM ignored : umlPM.getEdges()){
            lines.add(new Line());
            arrowHeadList.add(new Polygon());
        }
        for (int i = 0; i <= umlPM.getInheritanceDeepness(); i++) {
            inheritanceLevelHBox.add(new HBox(20));
        }
    }

    @Override
    public void layoutControls() {

        for (int i = 0; i < umlPM.getClassesObject().size(); i++) {
            logger.info("view class size "+ umlPM.getClassesObject().size());
            logger.info("view class classPane size " + umlClassPanes.size());
            logger.info("ihneritance size " + inheritanceLevelHBox.size());

            inheritanceLevelHBox.get(umlPM.getClassesObject().get(i).getInheritanceLevel())
                    .getChildren()
                    .add(umlClassPanes.get(i));
        }
        vBox.getChildren().addAll(inheritanceLevelHBox);

        getChildren().addAll(vBox);

        Platform.runLater(() -> {

            for (int i = 0; i < umlPM.getEdges().size(); i++){
                int finalI = i;
                Optional<UmlClassPane> source = umlClassPanes.stream().filter(
                        c -> c.getId().equals(umlPM.getEdges().get(finalI).getSource())).findFirst();
                Optional<UmlClassPane> target = umlClassPanes.stream().filter(
                        c -> c.getId().equals(umlPM.getEdges().get(finalI).getTarget())).findFirst();


                if (source.isPresent() && target.isPresent()) {
                    //getBoundsInParent() > get x Axis from hBox
                    lines.get(i).setStartX(source.get().getBoundsInParent().getCenterX());
                    //getBoundsInParent() > get y Axis from parent vBox
                    lines.get(i).setStartY(source.get().getParent().getBoundsInParent().getMinY());

                    lines.get(i).setEndX(target.get().getBoundsInParent().getCenterX());
                    lines.get(i).setEndY(target.get().getParent().getBoundsInParent().getMaxY());

                    if(umlPM.getEdges().get(i).getType().equals("extends")){
                        lines.get(i).getStyleClass().add("extendsLine");
                    } else if(umlPM.getEdges().get(i).getType().equals("implements")){
                        lines.get(i).getStyleClass().add("implementsLine");
                    }

                    //add arrow head to line
                    arrowHeadList.get(i).getPoints().addAll(
                            getArrowHeadForLine(lines.get(i))
                    );

                    linePane.getChildren().add(lines.get(i));
                    linePane.getChildren().add(arrowHeadList.get(i));


                } else {
                    logger.error("Class for Edge missing");
                }
            }
            getChildren().add(linePane);
        });

    }

    @Override
    public void setupValueChangedListeners() {
        umlPM.classesObjectProperty().addListener(pmStateListener);
    }

    private Double[] getArrowHeadForLine(Line line){

        double endX = line.getEndX();
        double endY = line.getEndY();
        double startX = line.getStartX();
        double startY = line.getStartY();

        double factor = 20 / Math.hypot(startX-endX, startY-endY);
        double factorO = 10 / Math.hypot(startX-endX, startY-endY);

        // part in direction of main line
        double dx = (startX - endX) * factor;
        double dy = (startY - endY) * factor;

        // part ortogonal to main line
        double ortX = (startX - endX) * factorO;
        double ortY = (startY - endY) * factorO;

        return new Double[]{ endX, endY,  endX + dx - ortY,endY + dy + ortX ,  endX + dx + ortY,endY + dy - ortX};
    }

}
