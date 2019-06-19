package inheritorgallery.view.util;


import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;

public class UmlClass extends Region {
    private Rectangle rectangle = new Rectangle();

    public UmlClass(){

        rectangle.setWidth(30.0f);
        rectangle.setHeight(15.0f);
        rectangle.setX(50f);
        rectangle.setY(50f);

        //drawingPane.getChildren().addAll(rectangle);

        getChildren().add(rectangle);
    }

}
