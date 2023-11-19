package de.woa;

import com.hsh.Evaluable;
import com.hsh.Fitness;
import com.hsh.parser.Node;
import de.woa.enums.VectorDefinition;
import de.woa.exceptions.DoubleInitializationNotPermittedException;
import de.woa.exceptions.RandomNotFoundException;

import java.util.*;

public class SearchAgent extends Evaluable {

    private static final ArrayList<SearchAgent> searchAgents = new ArrayList<>();
    private final Fitness fitness;
    private Node[] path;

    // Path size // number of cities
    private final int n;

    // Doubles
    private double p = Math.random();
    private double l = Math.random() * 2 - 1;

    // Vectors
    private final HashMap<VectorDefinition, Vector> vectors = new HashMap<>();

    private static Evaluable best;

    public SearchAgent(Fitness fitness) {
        this.fitness = fitness;

        shufflePath();
        initializeVectors();
        this.n = path.length;
    }

    protected static void setLeader(Evaluable best) {
        SearchAgent.best = best;
        System.out.println("Refresh Leader");

    }

    private void initializeVectors() {
        vectors.put(VectorDefinition.r, Vector.generateRandomVector(fitness.getDataset().getSize()));
        vectors.put(VectorDefinition.C, vectors.get(VectorDefinition.r).scalarMultiply(2d));
        double smallA = 2;
        vectors.put(VectorDefinition.A, vectors.get(VectorDefinition.r).scalarMultiply(2).scalarMultiply(smallA).scalarAddition(-smallA));
    }

    public void evaluate(int currentIteration, int totalIteration) {
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
        for (int i = 0; i < best.getPath().size(); i++) {
            dLeader.add((double) best.getPath().get(i));
            p.add((double) this.getPath().get(i));
        }

        return (dLeader
                .multiply(this.vectors.get(VectorDefinition.C))
                .subtract(p)
                .getAbsoluteValue() / n) / n;
    }


    private int getK(int j) {
        int k;
        if (getP() < 0.5 && vectors.get(VectorDefinition.A).getAbsoluteValue() < 1) {
            double b = 1; //TODO b muss noch herausgefunden werden
            double D = getD();
            double innerTerm = D * Math.pow(Math.E, (b * l)) * Math.cos(2 * Math.PI * l) + j;
            k = (int) Math.floor(innerTerm); // (3.3) with Leader
            k += (int) Math.floor((innerTerm) / n); // (3.3) with Leader
            k += 1;

        } else {
            int x = j + (int) (vectors.get(VectorDefinition.C).divide(vectors.get(VectorDefinition.A)).scalarMultiply(n).getAbsoluteValue());
            k = x - n * (x / n) + 1;
        }
        k = Math.abs(k);
        if (k < 1 || k > n) {
            try {
                throw new IndexOutOfBoundsException("The value of the variable K ("+ k +") is not in the interval [1," + n + "].");
            } catch (IndexOutOfBoundsException e) {
                System.out.println(e.getMessage());
                k = Math.max(k, 1);
                k = Math.min(k, n);
            }
        }

        return k - 1;
    }

    public void updateRoute() throws RandomNotFoundException {
        if (this.getPath().hashCode() != best.getPath().hashCode()) {
            if (vectors.get(VectorDefinition.A).getAbsoluteValue() < 1) {
                System.out.println(this.getPath());
                for (int j = 0; j < path.length; j++) {
                    int k = getK(j);
                    // @TODO: Irgendwo hier ist der Bug. Die Elemente der Liste werden nicht vertauscht.
                    int id = best.getPath().get(k);
                    for (int i = 0; i < path.length; i++) {
                        if (id == path[i].getId()) {
                            Collections.swap(Arrays.asList(path), j, i);
                            break;
                        }
                    }
                }
                System.out.println(this.getPath());
                System.out.println("WALE FERTIG");
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
