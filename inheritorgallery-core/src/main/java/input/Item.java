package input;

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

    public void xParamCount(){ }
    public void xParamCount(int i1){}
    public void xParamCount(int i1, int i2){ }
    public void xParamCount(int i1, int i2, int i3){ }
}
