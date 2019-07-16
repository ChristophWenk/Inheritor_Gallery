package inheritorgallery.view.instances;


import inheritorgallery.view.SharedLayouter;
import inheritorgallery.view.ViewMixin;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import presentationmodel.ColorPM;
import presentationmodel.instance.ObjectPM;
import presentationmodel.instance.ReferencePM;
import presentationmodel.uml.ClassPM;
import presentationmodel.uml.FieldPM;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;


public class ObjectPartUnit extends VBox implements ViewMixin {
    private ArrayList<Label> methodLabels;
    private List<Label> references;
    private List<Label> fields;
    private Label className;
    private ClassPM classPM;
    private ColorPM colorPM;
    private Separator separator1,separator2;
    private SharedLayouter layouter;
    private List<ReferencePM> referencesList;

    public ObjectPartUnit(ClassPM classPM, ColorPM colorPM, List<ReferencePM> referencesList){
        this.classPM = classPM;
        this.getStyleClass().add("plainBorder");
        String color = colorPM.getColor(classPM.getFullClassName());
        this.setStyle("-fx-background-color:" + color);

        this.colorPM = colorPM;
        this.referencesList = referencesList;
        init();
    }

    @Override
    public void initializeControls() {
        layouter = new SharedLayouter();
        className = new Label(classPM.getName());

        references = new ArrayList<>();
        fields = new ArrayList<>();
        methodLabels = new ArrayList<>();
        separator1 = new Separator();
        separator2 = new Separator();

        for(FieldPM field : classPM.getFields()){
            fields.add(new Label(field.getName() + " " + field.getValue()));
        }

        for (int i = 0; i < classPM.getMethods().size(); i++) {
            methodLabels.add(new Label());
        }

        for (int i = 0; i < classPM.getMethods().size(); i++) {
            final int j = i;
            StringBinding binding = Bindings.createStringBinding(
                    () -> MessageFormat.format("{0} ({1})",
                            classPM.getMethods().get(j).getName(),
                            layoutMethodParameters(j)),
                    classPM.getMethods().get(j).nameProperty(),
                    classPM.getMethods().get(j).inputParametersProperty());

            methodLabels.get(j).textProperty().bind(binding);

            if(classPM.getMethods().get(j).getImplementedInClass() != null) {
                String color = colorPM.getColor(classPM.getMethods().get(j).getImplementedInClass());
                methodLabels.get(j).setStyle("-fx-background-color:" + color);
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

        getChildren().addAll(methodLabels);
    }

    @Override
    public void setupBindings() {
    }

    public String layoutMethodParameters(int currentMethod) {
        String parameters = "";
        int paramCount = classPM.getMethods().get(currentMethod).getInputParameters().size();
        int k = 0;

        for (String parameter : classPM.getMethods().get(currentMethod).getInputParameters()) {
            if (k < paramCount - 1) {
                parameters += (parameter + ", ");
            }
            else {
                parameters += parameter;
            }
            k++;
        }
        return parameters;
    }
}
