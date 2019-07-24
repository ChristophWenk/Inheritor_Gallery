package inheritorgallery.view.instances;

import inheritorgallery.view.ViewMixin;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import presentationmodel.ColorPM;
import presentationmodel.instance.ReferencePM;
import presentationmodel.uml.ClassPM;
import presentationmodel.uml.FieldPM;
import presentationmodel.uml.MethodPM;

import java.util.ArrayList;
import java.util.List;

public class ObjectPartUnit extends VBox implements ViewMixin {
    private ArrayList<Label> fieldLabels;
    private ArrayList<HBox> methodHBoxes;
    private HBox referenceHBox;
    private HBox classNameHBox;
    private ClassPM classPM;
    private ColorPM colorPM;
    private List<ReferencePM> referencesList;


    public ObjectPartUnit(ClassPM classPM, ColorPM colorPM, List<ReferencePM> referencesList){
        this.classPM = classPM;
        if(classPM.isIsInterface())   this.getStyleClass().add("dashedBorder");
        else                          this.getStyleClass().add("plainBorder");
        String color = colorPM.getColor(classPM.getFullClassName());
        this.setStyle("-fx-background-color:" + color);

        this.colorPM = colorPM;
        this.referencesList = referencesList;

        init();
    }

    @Override
    public void initializeControls() {
        setAlignment(Pos.CENTER);
        classNameHBox = new HBox(new Label(classPM.getName()));
        classNameHBox.setAlignment(Pos.BASELINE_CENTER);

        setAlignment(Pos.CENTER_LEFT);
        referenceHBox = new HBox();
        methodHBoxes = new ArrayList<>();
        fieldLabels = new ArrayList<>();

        for(FieldPM fieldPM : classPM.getFields())
            fieldLabels.add(new Label(
                    fieldPM.getName() + ": "+
                    fieldPM.getValue()));


        for (MethodPM methodPM : classPM.getMethods()) {

            StringBuilder stringBuilder = new StringBuilder();
            for(String inputParam : methodPM.getInputParameters()) stringBuilder.append(inputParam).append(", ");
            String inputParams = stringBuilder.toString();
            if(inputParams.length() > 0) inputParams = inputParams.substring(0, inputParams.length() - 2);
            inputParams = "("+inputParams+")";

            Label methodLabel = new Label(methodPM.getName() +  inputParams );
            HBox methodHBox = new HBox(methodLabel);

            if(methodPM.getImplementedInClass() != null){
                String color = colorPM.getColor(methodPM.getImplementedInClass());
                methodLabel.setStyle("-fx-background-color:" + color);
                // make method box of override method a bit higher,
                // so that "last-executed-method" background would be vilible
                methodHBox.setPadding(new Insets(2,0,2,2));
            }
            methodHBoxes.add(methodHBox);
        }

        if ((referencesList != null) && !(referencesList.isEmpty())) {
            StringBuilder stringBuilderRef = new StringBuilder();
            for(ReferencePM referencePM : referencesList)
                stringBuilderRef.append(referencePM.getReferenceName()).append(", ");
            String refsString = stringBuilderRef.toString();
            if(refsString.length() > 0) refsString = refsString.substring(0, refsString.length() - 2);

            Effect glow = new Glow(0.5);

            Text text = new Text(refsString);
            text.setFill(Color.WHITE);
            text.setEffect(glow);
            text.setFont(Font.font("Verdana", FontWeight.BOLD,12));


            referenceHBox.setAlignment(Pos.CENTER);
            referenceHBox.setTranslateY(-7);
            referenceHBox.toFront();
            referenceHBox.getChildren().add(text);
        }
    }

    @Override
    public void layoutControls() {
        this.minWidth(150);
        this.setPadding(new Insets(5,5,5,5));
        getChildren().add(referenceHBox);
        getChildren().add(classNameHBox);
        getChildren().add(new Separator());

        getChildren().addAll(fieldLabels);
        getChildren().add(new Separator());

        getChildren().addAll(methodHBoxes);
    }

    @Override
    public void setupBindings() {
        for(int i = 0; classPM.getMethods().size() > i; i++){
            methodHBoxes.get(i).styleProperty()
                    .bind(classPM.getMethods().get(i).lastExecutedAsStringProperty());
        }

    }
}
