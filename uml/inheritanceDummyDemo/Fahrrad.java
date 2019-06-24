package inheritanceDummyDemo;

public class Fahrrad extends Fahrzeug {

    public Fahrrad(String name, double geschwindigkeit) {
        super(name, geschwindigkeit);
    }

    public void print(){
        System.out.println(super.getName()+ " ist ein Fahrrad");
    }

}
