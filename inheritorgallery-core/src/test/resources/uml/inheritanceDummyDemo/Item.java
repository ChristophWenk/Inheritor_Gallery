package inheritanceDummyDemo;

public abstract class Item {
    private double weight;

    public Item(){
        this.weight = 0;
    }

    public abstract void print();

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
