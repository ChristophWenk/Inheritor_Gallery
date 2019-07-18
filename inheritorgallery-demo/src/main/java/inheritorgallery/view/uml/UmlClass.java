package inheritorgallery.view.uml;

import inheritorgallery.view.ViewMixin;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import presentationmodel.uml.ClassPM;
import presentationmodel.uml.ConstructorPM;
import presentationmodel.uml.FieldPM;
import presentationmodel.uml.MethodPM;

import java.util.ArrayList;


public class UmlClass extends VBox implements ViewMixin {
    private final ClassPM classPM;
    private Label classNameLabel;
    private ArrayList<UMLFieldPane> umlFieldPanes;
    private ArrayList<UMLConstructorPane> umlConstructorPanes;
    private ArrayList<UMLMethodPane> umlMethodPanes;

    UmlClass(ClassPM classPM){
        this.classPM = classPM;
        this.setId(classPM.getName());
        this.getStyleClass().add("plainBorder");
        this.setMinWidth(200);
        init();
    }

    @Override
    public void initializeControls() {
        classNameLabel = new Label(classPM.getName());

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
        getChildren().addAll(classNameLabel);
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
