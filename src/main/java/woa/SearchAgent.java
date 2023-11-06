package woa;

import com.hsh.parser.Dataset;
import com.hsh.parser.Node;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SearchAgent {

    public ArrayList<Integer> getP() {
        ArrayList<Integer> temp = new ArrayList<>();
        for(Double d : P) {
            temp.add(d.intValue());
        }
        return temp;
    }

    private Vector P;
    private double p;
    //private Vector<Double> a = new Vector<Double>(2d,2d);
    private double a;
    private double b;
    private double l;
    // TODO Aktuell zwischen [0,1) müsste aber [0,1]
    private Vector r = new Vector();
    private Vector A = new Vector();
    private Vector C = new Vector();
    private Dataset cities;
    private int maxIter;


    public SearchAgent(Dataset cities, int maxIter) {
        this.cities = cities;
        this.maxIter = maxIter;

        for (int i = 0; i < cities.getSize(); i++) r.add(Math.random());

        initRandomRoute(cities);

    }

    private void initRandomRoute(Dataset cities) {
        List<Node> list = Arrays.asList(cities.getNodes());
        Collections.shuffle(list);
        P = new Vector();
        for (Node node : list) {
            P.add((double) node.getId());
            // TODO: A & C zum testen
            A.add((double) node.getId());
            C.add((double) node.getId());
        }
    }

    private void calcSmallA(int maxIter, int currentIter) {
        a = 2 - 2 * ((double) maxIter / currentIter);
    }

    private void calcVecA() {
        for (int i = 0; i < r.size(); i++) {
            A.set(i, 2 * a * r.get(i) - a);
        }
    }

    private void calcR() {
        r.replaceAll(aDouble -> Math.random());
    }

    private void calcC() {
        for (int i = 0; i < r.size(); i++) {
            C.set(i, 2 * r.get(i));
        }
    }

    private void calcP() {
        p = Math.random();
    }

    private void calcL() {
        l = Math.random() * 2 - 1;
    }

    private void calcB() {
        //TODO: ????????????
        b = 0.1;
    }

    private double calcD(SearchAgent searchAgent) {
        Vector test = new Vector();
        Vector test2 = new Vector();
        test.add(2d);
        test2.add(4d);


        System.out.println((C.multiply(searchAgent.P).subtract(P)));
        System.out.println(C);
        System.out.println(searchAgent.P);
        System.out.println(P);

        System.out.println((C.multiply(searchAgent.P).subtract(P)).getAbsoluteValue());
        return (C.multiply(searchAgent.P).subtract(P)).getAbsoluteValue();
    }

    public void calcValues(int maxIter, int currentIter) {
        calcSmallA(maxIter, currentIter);
        calcVecA();
        calcR();
        calcC();
        calcP();
        calcL();
        calcB();
    }

    public void updatePosition(SearchAgent searchAgent, int currentIter) {
        calcValues(maxIter, currentIter);
        if (p < 0.00001) {
            if (A.getAbsoluteValue() < 1) {
                exploit1(searchAgent);
            } else {
                explore();
            }
        } else {
            exploit2(searchAgent, 5);
        }
    }

    /**
     * Exploration phase (Search for prey) (3.2)
     */
    private void explore() {

    }

    /**
     * Exploitation phase (Shrinking encircling mechanism) (3.2)
     */
    private void exploit1(SearchAgent leader) {

    }

    /**
     * Exploitation phase (Spiral updating position) (3.3)
     */
    private void exploit2(SearchAgent leader, int j) {
        double k;
        double D = calcD(leader);
        k = Math.floor(D*Math.pow(Math.E, (b * l))*Math.cos(2*Math.PI*l) + j);
        k += Math.floor((D*Math.pow(Math.E, (b * l))*Math.cos(2*Math.PI*l) + j) / cities.getSize());
        k += 1;
        System.out.println(k);
    }
}
