package inheritanceDummyDemo;

public class Fahrzeug extends Item {

    private double speed;

    public Fahrzeug(String name, double speed){
        this.name  = name;
        this.speed = speed;
    }

    public void print(){
        System.out.println("Fahrzeug: "+name+" fährt "+speed);
    }
    public void printFahrzeug(){
        System.out.println("Fahrzeug: "+name+" fährt "+speed);
    }

    @Override
    public Fahrzeug clone(){
        return new Fahrzeug(this.name, this.speed);
    }

    public boolean equals(Fahrzeug f){
        return f != null && f.name.equals(this.name);
    }

    @Override
    public boolean equals(Object obj){
        if (obj instanceof Fahrzeug){
            Fahrzeug f = (Fahrzeug) obj;
            return f.name.equals(this.name);
        }
        return false;
    }

    public String toString(){
        //System.out.println(name+" fährt "+speed);
        return "Fahrzeug "+name+" fährt "+speed;
    }

}
