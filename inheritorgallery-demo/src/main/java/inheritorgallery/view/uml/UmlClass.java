package inheritorgallery.view.uml;


import inheritorgallery.view.ViewMixin;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import presentationmodel.uml.ClassPM;
import presentationmodel.uml.MethodPM;

import java.lang.reflect.Method;
import java.util.ArrayList;


public class UmlClass extends VBox implements ViewMixin {
    private final ClassPM classPM;
    private Label classNameLabel;
    private ArrayList<Label> fieldLabels, constructorLabels, methodLabels;
    private Separator separator1,separator2;


    public UmlClass(ClassPM classPM){
        this.classPM = classPM;
        this.setId(classPM.getName());
        this.getStyleClass().add("classBox");
        init();
    }


    @Override
    public void initializeControls() {
        classNameLabel = new Label();
        classNameLabel.setId("classNameLabel");

        fieldLabels = new ArrayList<>();
        constructorLabels = new ArrayList<>();
        methodLabels = new ArrayList<>();
        separator1 = new Separator();
        separator2 = new Separator();

        for (int i = 0; i < classPM.getFields().size(); i++) {
            fieldLabels.add(new Label());
        }
        for (int i = 0; i < classPM.getConstructors().size(); i++) {
            constructorLabels.add(new Label());
        }
        for (int i = 0; i < classPM.getMethods().size(); i++) {
            methodLabels.add(new Label());
        }

    }

    @Override
    public void layoutControls() {

        getChildren().addAll(classNameLabel,separator1);


        for (int i = 0; i < classPM.getFields().size(); i++) {
            getChildren().add(fieldLabels.get(i));
        }
        getChildren().add(separator2);
        for (int i = 0; i < classPM.getConstructors().size(); i++) {
            getChildren().add(constructorLabels.get(i));
        }
        for (int i = 0; i < classPM.getMethods().size(); i++) {
            getChildren().add(methodLabels.get(i));
        }
    }

    @Override
    public void setupBindings() {
        classNameLabel.textProperty().bind(classPM.nameProperty());

        for (int i = 0; i < classPM.getFields().size(); i++) {
            fieldLabels.get(i).textProperty()
                    .bind(classPM.getFields().get(i).nameProperty());
        }

        for (int i = 0; i < classPM.getConstructors().size(); i++) {
            constructorLabels.get(i).textProperty()
                    .bind(classPM.getConstructors().get(i).nameProperty());
        }

        for (int i = 0; i < classPM.getMethods().size(); i++) {
            methodLabels.get(i).textProperty()
                    .bind(classPM.getMethods().get(i).nameProperty());
            //for (MethodPM method : classPM.getMethods()) {
                String parameters = "";
                int paramCount = classPM.getMethods().get(i).getInputParameters().size();
                int j = 0;
                for (String parameter : classPM.getMethods().get(i).getInputParameters()) {
                    if (j < paramCount - 1) {
                        parameters += (parameter + ", ");
                    }
                    else {
                        parameters += parameter;
                    }
                    methodLabels.get(i).textProperty().bind(Bindings.concat(
                            classPM.getMethods().get(i).nameProperty(),
                            " (",
                            parameters,
                            ")"
                    ));
                    j++;
                }
           // }

        }

    }
}
