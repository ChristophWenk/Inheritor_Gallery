package input;

public class Auto extends Fahrzeug {
    private int ps;
    private int color;

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

