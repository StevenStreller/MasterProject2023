package woa;

public class Vector<Num extends Number> {

    private Num x,y;

    public Vector(Num x, Num y) {
        this.x = x;
        this.y = y;
    }
    public Num getY() {
        return y;
    }

    public Num getX() {
        return x;
    }

    public double getAbsoluteValue() {
        return Math.sqrt(Math.pow((double) x, 2) + Math.pow((double) y, 2));
    }

    public Vector<Double> multiply(Vector<Num> v2) {
        return new Vector<Double>((double) x * (double) v2.x, (double) y * (double) v2.y);
    }

    public Vector<Double> subtract(Vector<Num> v2) {
        return new Vector<Double>((double) x - (double) v2.x, (double) y - (double) v2.y);
    }

    public Vector<Double> multiply(double scalar) {
        return new Vector<Double>( (double) x * scalar, (double) y * scalar);
    }

    public Vector<Double> add(double scalar) {
        return new Vector<Double>((double) x + scalar, (double) y + scalar);
    }

}
