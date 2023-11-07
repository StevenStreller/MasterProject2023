package de.woa;

import org.junit.jupiter.api.Test;
import woa.Vector;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class VectorTest {

    @Test
    void generateRandomVector() {
        int randomNumber = new Random().nextInt(100);
        Vector vector = Vector.generateRandomVector(randomNumber);
        assertEquals(vector.size(), randomNumber);
    }

    @Test
    void getAbsoluteValue() {
        Vector vector = new Vector();
        vector.add(6d);
        vector.add(2.5);
        assertEquals(vector.getAbsoluteValue(), 6.5);
    }

    @Test
    void scalarMultiply() {
        Vector vector = new Vector();
        vector.add(6d);
        vector.add(2.5);
        vector = vector.scalarMultiply(10);
        assertEquals(vector.get(0), 60d);
        assertEquals(vector.get(1), 25d);

        vector = vector.scalarMultiply(0.1);
        assertEquals(vector.get(0), 6d);
        assertEquals(vector.get(1), 2.5d);

        vector = vector.scalarMultiply(2).scalarMultiply(2);
        assertEquals(vector.get(0), 24d);
        assertEquals(vector.get(1), 10d);

    }

    @Test
    void scalarAddition() {
        Vector vector = new Vector();
        vector.add(6d);
        vector.add(2.5);
        vector = vector.scalarAddition(10);
        assertEquals(vector.get(0), 16d);
        assertEquals(vector.get(1), 12.5d);

        vector = vector.scalarAddition(1);
        assertEquals(vector.get(0), 17d);
        assertEquals(vector.get(1), 13.5d);

        vector = vector.scalarAddition(2).scalarAddition(2);
        assertEquals(vector.get(0), 21d);
        assertEquals(vector.get(1), 17.5d);
    }

    @Test
    void multiply() {
        Vector v1 = new Vector();
        v1.add(6d);
        v1.add(2.5);
        Vector v2 = new Vector();
        v2.add(10d);
        v2.add(5d);

        Vector result = v1.multiply(v2);
        assertEquals(result.get(0), 60d);
        assertEquals(result.get(1), 12.5);
    }

    @Test
    void subtract() {
        Vector v1 = new Vector();
        v1.add(6d);
        v1.add(2.5);
        Vector v2 = new Vector();
        v2.add(10d);
        v2.add(5d);

        Vector result = v2.subtract(v1);
        assertEquals(result.get(0), 4d);
        assertEquals(result.get(1), 2.5d);
    }
}