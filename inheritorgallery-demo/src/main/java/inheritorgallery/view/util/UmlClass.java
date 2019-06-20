package inheritorgallery.view.util;


import inheritorgallery.view.ViewMixin;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import presentationmodel.uml.ClassPM;

import java.util.ArrayList;


public class UmlClass extends VBox implements ViewMixin {
    private final ClassPM model;
    private Label classNameLabel;
    private ArrayList<Label> fieldLabels, constructorLabels, methodLabels;
    private Separator separator1,separator2,separator3;


    public UmlClass(ClassPM model){
        this.model = model;
        this.setId("classBox");
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
        separator3 = new Separator();

        for (int i = 0; i < model.getFields().size(); i++) {
            fieldLabels.add(new Label());
        }
        for (int i = 0; i < model.getConstructors().size(); i++) {
            constructorLabels.add(new Label());
        }
        for (int i = 0; i < model.getMethods().size(); i++) {
            methodLabels.add(new Label());
        }

    }

    @Override
    public void layoutControls() {

        getChildren().addAll(classNameLabel,separator1);


        for (int i=0 ; i < model.getFields().size(); i++) {
            getChildren().add(fieldLabels.get(i));
        }
        getChildren().add(separator2);
        for (int i=0 ; i < model.getConstructors().size(); i++) {
            getChildren().add(constructorLabels.get(i));
        }
        getChildren().add(separator3);
        for (int i=0 ; i < model.getMethods().size(); i++) {
            getChildren().add(methodLabels.get(i));
        }
    }

    @Override
    public void setupBindings() {
        classNameLabel.textProperty().bind(model.nameProperty());

        for (int i=0 ; i < model.getFields().size(); i++) {
            fieldLabels.get(i).textProperty()
                    .bind(model.getFields().get(i).nameProperty());
        }

        for (int i=0 ; i < model.getConstructors().size(); i++) {
            constructorLabels.get(i).textProperty()
                    .bind(model.getConstructors().get(i).nameProperty());
        }

        for (int i=0 ; i < model.getMethods().size(); i++) {
            methodLabels.get(i).textProperty()
                    .bind(model.getMethods().get(i).nameProperty());
        }

    }
}
