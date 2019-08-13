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
import presentationmodel.uml.MethodPM;

/**
 * View that displays the method part of an UML class
 */
public class UMLMethodPane extends BorderPane implements ViewMixin {

    private MethodPM methodPM;
    private Label returnType;
    private HBox methodContentHBox;


    UMLMethodPane(MethodPM methodPM){
        this.methodPM = methodPM;
        init();
    }

    @Override
    public void initializeControls() {
        methodContentHBox = new HBox();

        StringBuilder stringBuilder = new StringBuilder();
        for(String inputParam : methodPM.getInputParameters()) stringBuilder.append(inputParam).append(", ");
        String inputParams = stringBuilder.toString();
        if(inputParams.length() > 0) inputParams = inputParams.substring(0, inputParams.length() - 2);
        inputParams = "("+inputParams+")";

        StackPane methodIconStackPane =
                new StackPane(
                        new ImageView(
                                new Image("icons/method.png",25, 0, true, false)));

        StackPane modifierIconStackPane =
                new StackPane(
                        new ImageView(
                                methodPM.getModifier().equals("private") ?
                                        new Image("icons/private.png",0, 17, true, false) :
                                        new Image("icons/public.png", 0, 20, true, false)
                        ));
        modifierIconStackPane.setPrefWidth(25);

        methodIconStackPane.setPadding(new Insets(2,2,2,2));
        methodContentHBox.getChildren().addAll(
                methodIconStackPane,
                modifierIconStackPane,
                new Label(methodPM.getName()),
                new Label((inputParams))
        );

        methodContentHBox.setAlignment(Pos.CENTER);

        returnType = new Label(methodPM.getReturnType());
    }

    @Override
    public void layoutControls() {
        this.setLeft(methodContentHBox);
        this.setRight(returnType);

    }

    @Override
    public void setupBindings() {
        this.styleProperty().bind(methodPM.lastExecutedAsStringProperty());
    }
}
