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
import presentationmodel.uml.ConstructorPM;


public class UMLConstructorPane extends BorderPane implements ViewMixin {

    private ConstructorPM constructorPM;
    private HBox constructorContentHBox;


    UMLConstructorPane(ConstructorPM constructorPM){
        this.constructorPM = constructorPM;
        init();
    }

    @Override
    public void initializeControls() {
        constructorContentHBox = new HBox();

        StringBuilder stringBuilder = new StringBuilder();
        for(String inputParam : constructorPM.getInputParameters()) stringBuilder.append(inputParam).append(", ");
        String inputParams = stringBuilder.toString();
        if(inputParams.length() > 0) inputParams = inputParams.substring(0, inputParams.length() - 2);
        inputParams = "("+inputParams+")";

        StackPane methodIconStackPane =
                new StackPane(
                        new ImageView(
                                new Image("icons/constructor.png",25, 0, true, false)));

        StackPane modifierIconStackPane =
                new StackPane(
                        new ImageView(
                                constructorPM.getModifier().equals("private") ?
                                        new Image("icons/private.png",0, 17, true, false) :
                                        new Image("icons/public.png", 0, 20, true, false)
                        ));
        modifierIconStackPane.setPrefWidth(25);


        methodIconStackPane.setPadding(new Insets(2,2,2,2));
        constructorContentHBox.getChildren().addAll(
                methodIconStackPane,
                modifierIconStackPane,
                new Label(constructorPM.getName()),
                new Label((inputParams))
        );

        constructorContentHBox.setAlignment(Pos.CENTER);

    }

    @Override
    public void layoutControls() {
        this.setLeft(constructorContentHBox);

    }
}
