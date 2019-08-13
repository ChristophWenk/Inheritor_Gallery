package presentationmodel.instance;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Presentationmodel that stores the current state of a reference
 */
public class ReferencePM {

    private final StringProperty referenceType = new SimpleStringProperty();
    private final StringProperty referenceName = new SimpleStringProperty();

    /**
     * Create the ReferencePM
     * @param referenceType ObjectType of a reference
     * @param referenceName Name of a reference
     */
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
