package presentationmodel.uml;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import service.uml.ConstructorDTO;
import service.uml.FieldDTO;
import service.uml.MethodDTO;

import java.util.List;

public class ClassPM {

    private final IntegerProperty inheritanceLevel = new SimpleIntegerProperty();
    private final BooleanProperty isInterface = new SimpleBooleanProperty();
    private final StringProperty fullClassName = new SimpleStringProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty superClassName = new SimpleStringProperty();
    private final ObservableList<String> implementedInterfaces = FXCollections.observableArrayList();
    private final ObservableList<FieldPM> fields = FXCollections.observableArrayList();
    private final ObservableList<ConstructorPM> constructors = FXCollections.observableArrayList();
    private final ObservableList<MethodPM> methods = FXCollections.observableArrayList();


    public ClassPM(
            Boolean isInterface,
            String fullClassName,
            String name,
            String superClassName,
            List<String> implementedInterfaces,
            List<FieldDTO> fields,
            List<ConstructorDTO> constructors,
            List<MethodDTO> methods) {

        setIsInterface(isInterface);
        setFullClassName(fullClassName);
        setName(name);
        setSuperClassName(superClassName);
        this.implementedInterfaces.addAll(implementedInterfaces);

        for(FieldDTO f : fields){
            this.fields.add(new FieldPM(
                    f.getModifier(),
                    f.getType(),
                    f.getName(),
                    null));}

        for(ConstructorDTO c : constructors){
            this.constructors.add((new ConstructorPM(
                    c.getModifier(),
                    c.getName(),
                    c.getInputParameters())));}

        for(MethodDTO m : methods){this.methods.add((new MethodPM(m.getName())));}
        //for(EdgeDTO e : edgeDTOs){
        //    this.edges.add(new EdgePM( e.getSource() ,e.getTarget(), e.getType()));
        //}

    }

    public int getInheritanceLevel() {
        return inheritanceLevel.get();
    }

    public IntegerProperty inheritanceLevelProperty() {
        return inheritanceLevel;
    }

    public void setInheritanceLevel(int inheritanceLevel) {
        this.inheritanceLevel.set(inheritanceLevel);
    }

    public boolean isIsInterface() {
        return isInterface.get();
    }

    public BooleanProperty isInterfaceProperty() {
        return isInterface;
    }

    public void setIsInterface(boolean isInterface) {
        this.isInterface.set(isInterface);
    }

    public String getFullClassName() {
        return fullClassName.get();
    }

    public StringProperty fullClassNameProperty() {
        return fullClassName;
    }

    public void setFullClassName(String fullClassName) {
        this.fullClassName.set(fullClassName);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getSuperClassName() {
        return superClassName.get();
    }

    public StringProperty superClassNameProperty() {
        return superClassName;
    }

    public void setSuperClassName(String superClassName) {
        this.superClassName.set(superClassName);
    }

    public ObservableList<String> getImplementedInterfaces() {
        return implementedInterfaces;
    }

    public ObservableList<FieldPM> getFields() {
        return fields;
    }

    public ObservableList<ConstructorPM> getConstructors() {
        return constructors;
    }

    public ObservableList<MethodPM> getMethods() {
        return methods;
    }
}
