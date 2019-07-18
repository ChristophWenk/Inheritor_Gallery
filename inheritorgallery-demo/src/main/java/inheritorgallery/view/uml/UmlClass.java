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
    private ArrayList<Label> fieldLabels, constructorLabels, methodLabels;

    UmlClass(ClassPM classPM){
        this.classPM = classPM;
        this.setId(classPM.getName());
        this.getStyleClass().add("plainBorder");
        this.setMinWidth(100);
        init();
    }

    @Override
    public void initializeControls() {
        classNameLabel = new Label(classPM.getName());

        fieldLabels = new ArrayList<>();
        constructorLabels = new ArrayList<>();
        methodLabels = new ArrayList<>();

        for(FieldPM fieldPM : classPM.getFields())
            fieldLabels.add(new Label(fieldPM.getName()));

        for(ConstructorPM constructorPM : classPM.getConstructors())
            constructorLabels.add(new Label(constructorPM.getName()));

        for(MethodPM methodPM : classPM.getMethods())
            methodLabels.add(new Label(methodPM.getName()));
    }

    @Override
    public void layoutControls() {
        getChildren().addAll(classNameLabel);
        getChildren().add(new Separator());
        getChildren().addAll(fieldLabels);
        getChildren().add(new Separator());
        getChildren().addAll(constructorLabels);
        getChildren().addAll(methodLabels);
    }

    @Override
    public void setupBindings() {

    }
}
