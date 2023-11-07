package woa;

import java.util.ArrayList;

public class Vector extends ArrayList<Double> {

    /**
     * Creates a vector with any number of randomized entries (
     * @param entries
     * @return
     */
    public static Vector generateRandomVector(int entries) {
        Vector vector = new Vector();
        for (int i = 0; i < entries; i++) {
            vector.add(Math.random());
        }
        return vector;
    }


    public double getAbsoluteValue() {
        double value = 0;

        for (Number number : this) {
            value += Math.pow((double) number, 2);
        }

        return Math.sqrt(value);
    }

    public Vector scalarMultiply(double scalar) {
        Vector tmpVector = new Vector();
        for (int i = 0; i < this.size(); i++) {
            tmpVector.add(i, this.get(i) * scalar);
        }

        return tmpVector;
    }

    public Vector scalarAddition(double scalar) {
        Vector tmpVector = new Vector();
        for (int i = 0; i < this.size(); i++) {
            tmpVector.add(i, this.get(i) + scalar);
        }

        return tmpVector;
    }

    public Vector multiply(Vector v2) {
        Vector tmpVector = new Vector();
        for (int i = 0; i < this.size(); i++) {
            tmpVector.add(i, this.get(i) * v2.get(i));
        }

        return tmpVector;
    }

    public Vector subtract(Vector v2) {
        Vector tmpVector = new Vector();
        for (int i = 0; i < this.size(); i++) {
            tmpVector.add(i, this.get(i) - v2.get(i));
        }

        return tmpVector;
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
