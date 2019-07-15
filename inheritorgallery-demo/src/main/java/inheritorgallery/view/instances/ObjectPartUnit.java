package inheritorgallery.view.instances;


import inheritorgallery.view.ViewMixin;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import presentationmodel.ColorPM;
import presentationmodel.instance.ObjectPM;
import presentationmodel.instance.ReferencePM;
import presentationmodel.uml.ClassPM;
import presentationmodel.uml.FieldPM;
import presentationmodel.uml.MethodPM;

import java.util.ArrayList;
import java.util.List;


public class ObjectPartUnit extends VBox implements ViewMixin {
    private List<Label> references;
    private List<Label> fields;
    private List<Label> methods;
    private Label className;
    private ClassPM classPM;
    private ColorPM colorPM;
    private Separator separator1,separator2;
    private List<ReferencePM> referencesList;

    public ObjectPartUnit(ClassPM classPM, ColorPM colorPM, List<ReferencePM> referencesList){
        this.classPM = classPM;
        this.colorPM = colorPM;
        this.referencesList = referencesList;
        init();
    }

    @Override
    public void initializeControls() {
        className = new Label(classPM.getName());

        references = new ArrayList<>();
        fields = new ArrayList<>();
        methods = new ArrayList<>();
        separator1 = new Separator();
        separator2 = new Separator();

        for(FieldPM field : classPM.getFields()){
            fields.add(new Label(field.getName() + " " + field.getValue()));
        }

        for(MethodPM method : classPM.getMethods()){
            String parameters = "";
            int paramCount = method.getInputParameters().size();
            int i = 0;
            for (String parameter : method.getInputParameters()) {
                if (i < paramCount - 1) {
                    parameters += (parameter + ", ");
                }
                else {
                    parameters += parameter;
                }
                i++;
            }

            if(method.getImplementedInClass() != null){
                Label methodLabel = new Label(method.getName()+ " (" + parameters.toString() + ")");
                String color = colorPM.getColor(method.getImplementedInClass());
                methodLabel.setStyle("-fx-background-color:" + color);

                methods.add(methodLabel);
            }
            else {
                for (String parameter : method.getInputParameters()) {

                }
                methods.add(new Label(method.getName() + " (" + parameters.toString() + ")"));
            }
        }

        if ((referencesList != null) && !(referencesList.isEmpty())) {
            for (ReferencePM referencePM : referencesList) {
                Label referenceLabel = new Label(referencePM.getReferenceName());
                referenceLabel.getStyleClass().add("referenceLabel");
                references.add(referenceLabel);
            }
        }

    }

    @Override
    public void layoutControls() {
        getChildren().addAll(references);

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
