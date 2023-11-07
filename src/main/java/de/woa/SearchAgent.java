package de.woa;

import com.hsh.Evaluable;
import com.hsh.Fitness;
import com.hsh.parser.Node;
import de.woa.enums.VectorDefinition;
import de.woa.exceptions.DoubleInitializationNotPermittedException;
import de.woa.exceptions.LeaderNotFoundException;
import de.woa.exceptions.RandomNotFoundException;
import woa.Vector;

import java.util.*;

public class SearchAgent extends Evaluable {

    private static final ArrayList<SearchAgent> searchAgents = new ArrayList<>();
    private final Fitness fitness;
    private Node[] path;

    // Doubles
    private double p = Math.random();
    private double l = Math.random() * 2 - 1;

    // Vectors
    private HashMap<VectorDefinition, Vector> vectors = new HashMap<>();


    public SearchAgent(Fitness fitness) {
        this.fitness = fitness;
        shufflePath();
        initializeVectors();
    }

    private void initializeVectors() {
        vectors.put(VectorDefinition.r, Vector.generateRandomVector(fitness.getDataset().getSize()));
        vectors.put(VectorDefinition.C, vectors.get(VectorDefinition.r).scalarMultiply(2d));
        double smallA = 2;
        vectors.put(VectorDefinition.A, vectors.get(VectorDefinition.r).scalarMultiply(2)); //TODO: Formel falsch
    }

    public void evaluate(int currentIteration, int totalIteration) {
        // TODO: Evaluate a, A, C, l and p
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

    public static SearchAgent getRandom() throws RandomNotFoundException {
        SearchAgent random = searchAgents.get(new Random().nextInt(searchAgents.size()));
        if (random == null) {
            throw new RandomNotFoundException();
        }
        return random;
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


    public double getK() throws LeaderNotFoundException, RandomNotFoundException {
        double k;
        if (getP() < 0.5) {
            if (vectors.get(VectorDefinition.A).getAbsoluteValue() < 1) {
                SearchAgent leader = getLeader();
                //TODO: k= (3.3) (LEADER)
                k = -1;
            } else {
                SearchAgent random = getRandom();
                // Gibt Funktion getRandom()
                //TODO: k= (3.2) (RANDOM)
                k = -1;
            }
        } else {
            SearchAgent leader = getLeader();
            // TODO: k= (3.2) (LEADER)
            k = -1;
        }
        return k;
    }

    public void updateRoute() {
        if (vectors.get(VectorDefinition.A).getAbsoluteValue() < 1) {
            // TODO (3.1)
        } else {
            // TODO (3.1)
        }
    }

}
