package presentationmodel.uml;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Presentationmodel that stores the state of an edge (line in UML)
 */
public class EdgePM {
    private final StringProperty source = new SimpleStringProperty();
    private final StringProperty target = new SimpleStringProperty();
    private final StringProperty type = new SimpleStringProperty();

    /**
     * Create the EdgePM
     * @param source Class where the edge is drawn from
     * @param target Class where the edge is drawn to
     * @param edgeType Type of the edge (e.g. interface)
     */
    public EdgePM(String source, String target, String edgeType){
        setSource(source);
        setTarget(target);
        setType(edgeType);

    }

    public String getSource() {
        return source.get();
    }

    public StringProperty sourceProperty() {
        return source;
    }

    public void setSource(String source) {
        this.source.set(source);
    }

    public String getTarget() {
        return target.get();
    }

    public StringProperty targetProperty() {
        return target;
    }

    public void setTarget(String target) {
        this.target.set(target);
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }
}
