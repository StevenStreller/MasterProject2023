package de.woa;

import com.hsh.Evaluable;
import com.hsh.Fitness;
import com.hsh.parser.Node;
import de.woa.enums.VectorDefinition;
import de.woa.exceptions.DoubleInitializationNotPermittedException;
import de.woa.exceptions.LeaderNotFoundException;
import de.woa.exceptions.RandomNotFoundException;

import java.util.*;

public class SearchAgent extends Evaluable {

    private static final ArrayList<SearchAgent> searchAgents = new ArrayList<>();
    private final Fitness fitness;
    private Node[] path;

    // Path size // number of cities
    private int n;

    // Doubles
    private double p = Math.random();
    private double l = Math.random() * 2 - 1;

    // Vectors
    private final HashMap<VectorDefinition, Vector> vectors = new HashMap<>();

    private SearchAgent leader;

    public SearchAgent(Fitness fitness) {
        this.fitness = fitness;
        this.leader = this;
        shufflePath();
        initializeVectors();
        this.n = path.length;
    }

    private void initializeVectors() {
        vectors.put(VectorDefinition.r, Vector.generateRandomVector(fitness.getDataset().getSize()));
        vectors.put(VectorDefinition.C, vectors.get(VectorDefinition.r).scalarMultiply(2d));
        double smallA = 2;
        vectors.put(VectorDefinition.A, vectors.get(VectorDefinition.r).scalarMultiply(2).scalarMultiply(smallA).scalarAddition(-smallA));
    }

    public void evaluate(int currentIteration, int totalIteration, SearchAgent leader) {
        this.leader = leader;
        double smallA = 2 - 2 * ((double) currentIteration / totalIteration);
        vectors.replace(VectorDefinition.r, Vector.generateRandomVector(fitness.getDataset().getSize())); // Muss r geupdatet werden?
        vectors.replace(VectorDefinition.C, vectors.get(VectorDefinition.r).scalarMultiply(2d));
        vectors.replace(VectorDefinition.A, vectors.get(VectorDefinition.r).scalarMultiply(2).scalarMultiply(smallA).scalarAddition(-smallA));
        l = Math.random() * 2 - 1;
        p = Math.random();
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

    private double getD() {
        Vector dLeader = new Vector();
        Vector p = new Vector();
        for (int i = 0; i < leader.getPath().size(); i++) {
            dLeader.add((double) leader.getPath().get(i));
            p.add((double) this.getPath().get(i));
        }

        return dLeader
                .multiply(this.vectors.get(VectorDefinition.C))
                .subtract(p)
                .getAbsoluteValue();
    }


    private int getK(int j) {
        int k;
        if (getP() < 0.5 && vectors.get(VectorDefinition.A).getAbsoluteValue() < 1) {
            double b = 1; //TODO b muss noch herausgefunden werden
            double D = getD() / n;
            k = (int) Math.floor(D * Math.pow(Math.E, (b * l)) * Math.cos(2 * Math.PI * l) + j); // (3.3) with Leader
            k += (int) Math.floor((D * Math.pow(Math.E, (b * l)) * Math.cos(2 * Math.PI * l) + j) / n); // (3.3) with Leader
            k += 1;
            if (k > path.length) {
                System.out.println("RIP: " + (path.length - k));
                k = k + (path.length - k);
            }
            k = Math.abs(k);
        } else {
            int x = j + (int) (vectors.get(VectorDefinition.C).divide(vectors.get(VectorDefinition.A)).scalarMultiply(n).getAbsoluteValue());
            k = x - n * (x / n) + 1;
        }
        return k - 1;
    }

    public void updateRoute() throws LeaderNotFoundException, RandomNotFoundException {
        if (!this.equals(leader)) {
            if (vectors.get(VectorDefinition.A).getAbsoluteValue() < 1) {
                for (int j = 0; j < path.length; j++) {
                    int k = getK(j);
                    int id = getLeader().path[k].getId();
                    for (int i = 0; i < path.length; i++) {
                        if (id == path[i].getId()) {
                            Collections.swap(Arrays.asList(path), j, i);
                            break;
                        }
                    }
                }
            } else {
                for (int j = 0; j < path.length; j++) {
                    int k = getK(j);
                    int id = getRandom().path[k].getId();
                    for (int i = 0; i < path.length; i++) {
                        if (id == path[i].getId()) {
                            Collections.swap(Arrays.asList(path), j, i);
                            break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(new Object[]{this.path});
    }
}
