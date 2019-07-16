package inheritorgallery.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.scene.control.Label;
import presentationmodel.uml.ClassPM;

import java.text.MessageFormat;
import java.util.ArrayList;

public class SharedLayouter {


    public void setupMethodBindings (int currentMethod, ClassPM classPM, ArrayList<Label> methodLabels) {
        StringBinding binding = Bindings.createStringBinding(
                () -> MessageFormat.format("{0} ({1})",
                        classPM.getMethods().get(currentMethod).getName(),
                        layoutMethodParameters(currentMethod,classPM)),
                classPM.getMethods().get(currentMethod).nameProperty(),
                classPM.getMethods().get(currentMethod).inputParametersProperty());

        methodLabels.get(currentMethod).textProperty().bind(binding);
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
