package inheritorgallery.view.util;


import inheritorgallery.view.ViewMixin;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import presentationmodel.uml.ClassPM;

import java.util.ArrayList;

public class UmlClass extends VBox implements ViewMixin {
    private final ClassPM model;
    private Rectangle rectangle;
    private Label classNameLabel;
    private ArrayList<Label> methodLabels;

    public UmlClass(ClassPM model){
        this.model = model;
        init();
        //drawingPane.getChildren().addAll(rectangle);

    }


    @Override
    public void initializeControls() {
        rectangle = new Rectangle();
        classNameLabel = new Label();
        methodLabels = new ArrayList<>();

        for (int i = 0; i < model.getMethods().size(); i++) {
            methodLabels.add(new Label());
        }
    }

    @Override
    public void layoutControls() {
        rectangle.setWidth(30.0f);
        rectangle.setHeight(15.0f);
        rectangle.setX(50f);
        rectangle.setY(50f);

        getChildren().addAll(rectangle, classNameLabel);

        for (int i=0 ; i < model.getMethods().size(); i++) {
            getChildren().add(methodLabels.get(i));
        }
    }

    @Override
    public void setupBindings() {
        classNameLabel.textProperty().bind(model.nameProperty());

        for (int i=0 ; i < model.getMethods().size(); i++) {
            methodLabels.get(i).textProperty()
                    .bind(model.getMethods().get(i).nameProperty());
        }

    }
}
