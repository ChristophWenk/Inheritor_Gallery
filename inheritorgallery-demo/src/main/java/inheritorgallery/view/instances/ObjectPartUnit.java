package inheritorgallery.view.instances;

import inheritorgallery.view.SharedLayouter;
import inheritorgallery.view.ViewMixin;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import presentationmodel.ColorPM;
import presentationmodel.instance.ReferencePM;
import presentationmodel.uml.ClassPM;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class ObjectPartUnit extends VBox implements ViewMixin {
    private ArrayList<Label> fieldLabels;
    private ArrayList<Label> methodLabels;
    private ArrayList<Label> referenceLabels;
    private Label className;
    private ClassPM classPM;
    private ColorPM colorPM;
    private List<ReferencePM> referencesList;
    private Separator separator1,separator2;
    private SharedLayouter layouter;

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

        referenceLabels = new ArrayList<>();
        methodLabels = new ArrayList<>();
        fieldLabels = new ArrayList<>();
        separator1 = new Separator();
        separator2 = new Separator();

        for (int i = 0; i < classPM.getFields().size(); i++) {
            fieldLabels.add(new Label());
        }
        for (int i = 0; i < classPM.getMethods().size(); i++) {
            methodLabels.add(new Label());
        }

        if ((referencesList != null) && !(referencesList.isEmpty())) {
            for (int i = 0; i < referencesList.size(); i++) {
                referenceLabels.add(new Label());
            }
        }
    }

    @Override
    public void layoutControls() {
        getChildren().addAll(referenceLabels);

        getChildren().add(className);
        getChildren().add(separator1);

        getChildren().addAll(fieldLabels);
        getChildren().add(separator2);

        getChildren().addAll(methodLabels);
    }

    @Override
    public void setupBindings() {
        if ((referencesList != null) && !(referencesList.isEmpty())) {
            for (int i = 0; i < referencesList.size(); i++) {
                ReferencePM referencePM = referencesList.get(i);
                referenceLabels.get(i).textProperty().bind(referencePM.referenceNameProperty());
                referenceLabels.get(i).getStyleClass().add("referenceLabel");
            }
        }

        for (int i = 0; i < classPM.getFields().size(); i++) {
            final int j = i;
            StringBinding binding = Bindings.createStringBinding(
                    () -> MessageFormat.format("{0}: {1}",
                            classPM.getFields().get(j).getName(),
                            classPM.getFields().get(j).getValue(),
                            classPM.getFields().get(j).nameProperty(),
                            classPM.getFields().get(j).valueProperty()));

            fieldLabels.get(j).textProperty().bind(binding);
        }

        for (int i = 0; i < classPM.getMethods().size(); i++) {
            layouter.setupMethodBindings(i,classPM,methodLabels);

            if(classPM.getMethods().get(i).getImplementedInClass() != null) {
                String color = colorPM.getColor(classPM.getMethods().get(i).getImplementedInClass());
                methodLabels.get(i).setStyle("-fx-background-color:" + color);
            }
        }
    }
}
