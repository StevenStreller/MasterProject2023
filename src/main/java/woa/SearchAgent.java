package woa;

import com.hsh.parser.Dataset;
import com.hsh.parser.Node;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SearchAgent {

    private int[] route;
    private double p;
    private Vector<Double> a = new Vector<Double>(2d,2d);
    // TODO Aktuell zwischen [0,1) müsste aber [0,1]
    private Vector<Double> r = new Vector<Double>(Math.random(), Math.random());

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

}
