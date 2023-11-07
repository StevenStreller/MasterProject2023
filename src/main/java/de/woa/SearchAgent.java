package de.woa;

import com.hsh.Evaluable;
import com.hsh.Fitness;
import com.hsh.parser.Node;
import de.woa.enums.Route;
import de.woa.exceptions.DoubleInitializationNotPermittedException;
import de.woa.exceptions.LeaderNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class SearchAgent extends Evaluable {

    private static final ArrayList<SearchAgent> searchAgents = new ArrayList<>();
    private final Fitness fitness;
    private Node[] path;

    private double p = Math.random();

    public SearchAgent(Fitness fitness) {
        this.fitness = fitness;
        shufflePath();
    }

    /**
     * Randomizes the order of nodes in the route
     */
    private void shufflePath() {
        this.path = fitness.getDataset().getNodes();
        Collections.shuffle(Arrays.asList(path));
    }


    public static void initializeSearchAgents(Fitness fitness, int population) throws DoubleInitializationNotPermittedException {
        if (!searchAgents.isEmpty()) {
            throw new DoubleInitializationNotPermittedException();
        } else {
            for (int i = 0; i < population; i++) {
                searchAgents.add(new SearchAgent(fitness));
            }
        }
    }

    public static ArrayList<SearchAgent> getSearchAgents() {
        return searchAgents;
    }

    /**
     * @return the SearchAgent with the shortest route
     * @throws LeaderNotFoundException Throws an exception if no leader (i.e. a SearchAgent with a short length) is found in the ArrayList.
     */
    public static SearchAgent getLeader() throws LeaderNotFoundException {
        int index = -1;
        int lowestFitness = Integer.MAX_VALUE;
        for (int i = 0; i < searchAgents.size(); i++) {
            int currentFitness = searchAgents.get(i).fitness.evaluate(searchAgents.get(i).getPath(), 0).getFitness();

            if ((lowestFitness > currentFitness) && (currentFitness != -1)) {
                lowestFitness = currentFitness;
                index = i;
            }
        }
        if (index == -1) {
            throw new LeaderNotFoundException();
        }
        return searchAgents.get(index);
    }

    public double getP() {
        return p;
    }

    @Override
    public ArrayList<Integer> getPath() {
        ArrayList<Integer> pathIndex = new ArrayList<>();
        for (Node node : this.path) {
            pathIndex.add(node.getId());
        }
        return pathIndex;
    }

    public void getK(Route route) {
        double k;
        if (route == Route.LEADER) {
            //TODO: Formel 3.3 muss implementiert werden
            k = 3.3;
        } else if (route == Route.RANDOM) {
            //TODO: Formel 3.2 muss implementiert werden
            k = 3.2;
        }
    }

    public void updateRoute(Route route) {
        if (route == Route.LEADER) {
            //TODO hier ist A < 1 (3.1)
        } else if (route == Route.RANDOM) {
            //TODO hier ist A >= 1 (3.1)
        }
    }

    public int getA() {
        //TODO A muss noch implementiert werden
        return -1;
    }
}
