package input;

public class Auto extends Fahrzeug {
    private int ps;
    private int color;
    private Object refTypeObjectNull;
    private Object refTypeObject = new Object();
    private Person p = new Person();

    public Auto(String name, double speed, int ps, int color) {
        super(name, speed);
        this.ps = ps;
        this.color = color;
    }

    @Override
    public void print() {
        System.out.println("Auto:     "+super.getName()+" fährt "+super.getSpeed());
    }

    //Getter und Setter

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

}

