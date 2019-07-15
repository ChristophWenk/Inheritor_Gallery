package inheritorgallery.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.scene.control.Label;
import presentationmodel.uml.ClassPM;

import java.text.MessageFormat;
import java.util.ArrayList;

public class SharedLayouter {


    public void layoutMethod (int currentMethod, ClassPM classPM, ArrayList<Label> methodLabels) {
        final int j = currentMethod;
        StringBinding binding = Bindings.createStringBinding(
                () -> MessageFormat.format("{0} ({1})",
                        classPM.getMethods().get(j).getName(),
                        layoutMethodParameters(j,classPM)),
                classPM.getMethods().get(j).nameProperty(),
                classPM.getMethods().get(j).inputParametersProperty());

        methodLabels.get(j).textProperty().bind(binding);
    }

    public String layoutMethodParameters(int currentMethod, ClassPM classPM) {
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
