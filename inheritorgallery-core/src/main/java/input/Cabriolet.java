package input;

public class Cabriolet extends Auto {
    private boolean verdeck;

    public Cabriolet(String name, double speed, int ps, int color) {
        super(name, speed, ps, color);
        verdeck = false;
    }

}
