package inheritorgallery.view.instances;


import inheritorgallery.view.ViewMixin;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import presentationmodel.uml.ClassPM;
import presentationmodel.uml.FieldPM;
import presentationmodel.uml.MethodPM;

import java.util.ArrayList;
import java.util.List;


public class ObjectPartUnit extends VBox implements ViewMixin {
    private List<Label> fields;
    private List<Label> methods;
    private Label className;
    private ClassPM model;
    private Separator separator1,separator2;

    public ObjectPartUnit(ClassPM model){
        this.model = model;
        init();
    }

    @Override
    public void initializeControls() {
        className = new Label(model.getName());

        fields = new ArrayList<>();
        methods = new ArrayList<>();
        separator1 = new Separator();
        separator2 = new Separator();

        for(FieldPM field : model.getFields()){
            fields.add(new Label(field.getName() + " " + field.getValue()));
        }

        for(MethodPM method : model.getMethods()){
            if(method.getImplementedInClass() != null){
                methods.add(new Label(method.getName()+ " " + method.getInputParameters() +" " + method.getImplementedInClass()));
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
        //objectLabel.textProperty().bind(model.objectFullNameProperty());
    }
}
