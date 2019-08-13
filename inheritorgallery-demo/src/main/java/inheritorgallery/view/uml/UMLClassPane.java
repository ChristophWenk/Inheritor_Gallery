package inheritorgallery.view.uml;

import inheritorgallery.view.ViewMixin;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import presentationmodel.uml.ClassPM;
import presentationmodel.uml.ConstructorPM;
import presentationmodel.uml.FieldPM;
import presentationmodel.uml.MethodPM;

import java.util.ArrayList;

/**
 * View that displays a whole UML class
 */
public class UMLClassPane extends VBox implements ViewMixin {
    private final ClassPM classPM;
    private ArrayList<UMLFieldPane> umlFieldPanes;
    private ArrayList<UMLConstructorPane> umlConstructorPanes;
    private ArrayList<UMLMethodPane> umlMethodPanes;
    private HBox classNameHBox;

    UMLClassPane(ClassPM classPM){
        this.classPM = classPM;
        this.setId(classPM.getName());
        this.getStyleClass().add("plainBorder");
        init();
    }

    @Override
    public void initializeControls() {

        StackPane classIconStackPane =
                new StackPane(
                        new ImageView(
                                classPM.isIsInterface() ?
                                        new Image("icons/interface.png",0, 17, true, false) :
                                        new Image("icons/class.png", 0, 20, true, false)
                        ));
        classIconStackPane.setPadding(new Insets(2,2,2,2));

        classNameHBox = new HBox(
                classIconStackPane,
                classPM.isIsAbstract() ? new Label(" <abstract> ") : new Label(""),
                new Label(classPM.getName()));
        classNameHBox.setAlignment(Pos.CENTER);



        umlFieldPanes = new ArrayList<>();
        umlConstructorPanes = new ArrayList<>();
        umlMethodPanes = new ArrayList<>();

        for(FieldPM fieldPM : classPM.getFields())
            umlFieldPanes.add(new UMLFieldPane(fieldPM));

        for(ConstructorPM constructorPM : classPM.getConstructors())
            umlConstructorPanes.add(new UMLConstructorPane(constructorPM));

        for(MethodPM methodPM : classPM.getMethods())
            umlMethodPanes.add(new UMLMethodPane(methodPM));
    }

    @Override
    public void layoutControls() {
        getChildren().addAll(classNameHBox);
       getChildren().add(new Separator());
        getChildren().addAll(umlFieldPanes);
        getChildren().add(new Separator());
        getChildren().addAll(umlConstructorPanes);
        getChildren().addAll(umlMethodPanes);
    }

    @Override
    public void setupBindings() {
    }
}
