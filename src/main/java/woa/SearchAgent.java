package woa;

import com.hsh.parser.Dataset;
import com.hsh.parser.Node;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SearchAgent {

    private int[] route;
    private double p;
    //private Vector<Double> a = new Vector<Double>(2d,2d);
    private double a;
    private double b;
    private double l;
    // TODO Aktuell zwischen [0,1) müsste aber [0,1]
    private Vector<Double> r = new Vector<Double>(Math.random(), Math.random());
    private Vector<Double> A;
    private Vector<Double> C;


    public SearchAgent(Dataset cities) {
        initRandomRoute(cities);

    }

    private void initRandomRoute(Dataset cities) {
        route = new int[cities.getSize()];
        List<Node> list = Arrays.asList(cities.getNodes());
        Collections.shuffle(list);
        for (int i = 0; i < list.size(); i++) {
            route[i] = list.get(i).getId();
        }
    }

    private void calcSmallA(int maxIter, int currentIter) {
        a = 2 - 2 * ((double) maxIter / currentIter);
    }

    private void calcVecA() {
        A = new Vector<>(2 * a * r.getX() - a, 2 * a * r.getY() - a);
    }

    private void calcR() {
        r = new Vector<Double>(Math.random(), Math.random());
    }

    private void calcC() {
        C = new Vector<>(2 * r.getX(), 2 * r.getY());
    }

    private void calcP() {
        p = Math.random();
    }

    private void calcL() {
        l = Math.random() * 2 - 1;
    }

    private void calcB() {
        //TODO: ????????????
        b = 1;
    }

    private void calcValues(int maxIter, int currentIter) {
        calcSmallA(maxIter, currentIter);
        calcVecA();
        calcR();
        calcC();
        calcP();
    }

    public void updatePosition(int[] routeOfBestAgent) {
        if (p < 0.5) {
            if (A.getAbsoluteValue() < 1) {
                exploit(routeOfBestAgent);
            } else {
                explore();
            }
        } else {
            exploit(routeOfBestAgent);
        }
    }

    /**
     * Exploration phase (Search for prey)
     */
    private void explore() {

    }

    /**
     * Exploitation phase (Bubble-Net Attack)
     */
    private void exploit(int[] routeOfBestAgent) {
        if (p < 0.5) {
            // shrinking encircling mechanism update
        } else if (p >= 0.5) {
            // spiral updating position
        }
    }
}
