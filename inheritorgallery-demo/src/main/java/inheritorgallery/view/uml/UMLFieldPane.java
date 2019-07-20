package inheritorgallery.view.uml;

import inheritorgallery.view.ViewMixin;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import presentationmodel.uml.FieldPM;


public class UMLFieldPane extends BorderPane implements ViewMixin {

    private FieldPM fieldPM;
    private Label typeLabel;
    private HBox fieldContentHBox;


    UMLFieldPane(FieldPM fieldPM){
        this.fieldPM = fieldPM;
        init();
    }

    @Override
    public void initializeControls() {
        fieldContentHBox = new HBox();

        StackPane attributeIconStackPane =
            new StackPane(
                new ImageView(
                        new Image("icons/attribute.png",25, 0, true, false)));

        StackPane modifierIconStackPane =
                new StackPane(
                        new ImageView(
                                fieldPM.getModifier().equals("private") ?
                                new Image("icons/private.png",0, 17, true, false) :
                                new Image("icons/public.png", 0, 20, true, false)
                        ));
        modifierIconStackPane.setPrefWidth(25);


        attributeIconStackPane.setPadding(new Insets(2,2,2,2));
        fieldContentHBox.getChildren().addAll(
                attributeIconStackPane,
                modifierIconStackPane,
                new Label(fieldPM.getName())

        );

        fieldContentHBox.setAlignment(Pos.CENTER);

        typeLabel = new Label(fieldPM.getType());
    }

    @Override
    public void layoutControls() {
        this.setLeft(fieldContentHBox);
        this.setRight(typeLabel);

    }
}
