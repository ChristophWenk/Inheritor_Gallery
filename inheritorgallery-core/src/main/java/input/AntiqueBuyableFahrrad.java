package input;

public class AntiqueBuyableFahrrad extends Fahrrad implements Antique, Buyable {

    private String epoque;
    private double price;

    public AntiqueBuyableFahrrad(String name, double geschwindigkeit, String epoque, double price) {
        super(name, geschwindigkeit);
        this.epoque = epoque;
        this.price = price;
    }

    @Override
    public String getEpoque() {
        return this.epoque;
    }

    @Override
    public double getPrice() {
        return this.price;
    }
}
