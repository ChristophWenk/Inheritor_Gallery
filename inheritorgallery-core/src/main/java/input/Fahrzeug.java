package input;

public class Fahrzeug extends Item {

    private double speed;
    String name;
    private static double dieselTax;
    public static final double gravity = 9.81;

    public Fahrzeug(String name, double speed){
        this.name  = name;
        this.speed = speed;
    }

    public void print(){
        System.out.println("Fahrzeug: "+name+" fährt "+speed);
    }

    public void print(String s){
        System.out.println("Fahrzeug: "+name+" fährt "+speed);
    }
    public void printFahrzeug(){
        System.out.println("Fahrzeug: "+name+" fährt "+speed);
    }

    public String toString(){
        //System.out.println(name+" fährt "+speed);
        return "Fahrzeug "+name+" fährt "+speed;
    }

    //Getter und Setter
    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public static void setDieselTax(double tax){
        dieselTax = tax;
    }
    public double getDieselTax(){
        return dieselTax;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
