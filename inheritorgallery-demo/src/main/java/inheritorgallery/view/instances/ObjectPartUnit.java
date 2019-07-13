package inheritorgallery.view.instances;


import inheritorgallery.view.ViewMixin;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import presentationmodel.ColorPM;
import presentationmodel.uml.ClassPM;
import presentationmodel.uml.FieldPM;
import presentationmodel.uml.MethodPM;

import java.util.ArrayList;
import java.util.List;


public class ObjectPartUnit extends VBox implements ViewMixin {
    private List<Label> fields;
    private List<Label> methods;
    private Label className;
    private ClassPM classPM;
    private ColorPM colorPM;
    private Separator separator1,separator2;

    public ObjectPartUnit(ClassPM classPM, ColorPM colorPM){
        this.classPM = classPM;
        this.colorPM = colorPM;
        init();
    }

    @Override
    public void initializeControls() {
        className = new Label(classPM.getName());

        fields = new ArrayList<>();
        methods = new ArrayList<>();
        separator1 = new Separator();
        separator2 = new Separator();

        for(FieldPM field : classPM.getFields()){
            fields.add(new Label(field.getName() + " " + field.getValue()));
        }

        for(MethodPM method : classPM.getMethods()){
            if(method.getImplementedInClass() != null){
                Label methodLabel = new Label(method.getName()+ " " + method.getInputParameters() +" " + method.getImplementedInClass());
                String color = colorPM.getColor(method.getImplementedInClass());
                methodLabel.setStyle("-fx-background-color:" + color);

                methods.add(methodLabel);
            }
            else {
                methods.add(new Label(method.getName() + " " + method.getInputParameters()));
            }
        }

    }

    @Override
    public void layoutControls() {
        getChildren().add(className);
        getChildren().add(separator1);

        getChildren().addAll(fields);
        getChildren().add(separator2);

        getChildren().addAll(methods);
    }

    @Override
    public void setupBindings() {
    }
}
