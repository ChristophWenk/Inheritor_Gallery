package inheritorgallery.view;

import inheritorgallery.view.util.UmlClass;
import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
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
        hBoxes = new ArrayList<>();
        vBox = new VBox(40);
        linePane = new Pane();
        scrollPane = new ScrollPane();

        for(ClassPM c : model.getClasses()){
            umlClasses.add(new UmlClass(c));
        }

        for(EdgePM ignored : model.getEdges()){
            lines.add(new Line());
        }
        for (int i = 0; i <= model.getInheritanceDeepness(); i++) {
            hBoxes.add(new HBox(20));
        }


    }

    @Override
    public void layoutControls() {
        for (int i=0 ; i < model.getClasses().size(); i++) {
            hBoxes.get(model.getInheritanceLevelOfClass(model.getClasses().get(i)))
                    .getChildren()
                    .add(umlClasses.get(i));
        }
        vBox.getChildren().addAll(hBoxes);

        //scrollPane.setContent(vBox);
        //scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        //scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

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


                    linePane.getChildren().add(lines.get(i));

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

}
