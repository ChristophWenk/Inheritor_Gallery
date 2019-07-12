package presentationmodel.instance;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ReferencePM {

    private final StringProperty referenceType = new SimpleStringProperty();
    private final StringProperty referenceName = new SimpleStringProperty();


    public ReferencePM(String referenceType, String referenceName ){
        setReferenceType(referenceType);
        setReferenceName(referenceName);
    }

    public String getReferenceType() {
        return referenceType.get();
    }

    public StringProperty referenceTypeProperty() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType.set(referenceType);
    }

    public String getReferenceName() {
        return referenceName.get();
    }

    public StringProperty referenceNameProperty() {
        return referenceName;
    }

    public void setReferenceName(String referenceName) {
        this.referenceName.set(referenceName);
    }


}
