package de.woa.searchagent;

import com.hsh.Evaluable;
import com.hsh.Fitness;
import com.hsh.parser.Node;
import de.woa.Vector;
import de.woa.enums.VectorDefinition;

import java.util.*;

public class AbstractSearchAgent extends Evaluable {

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

    public AbstractSearchAgent(Fitness fitness) {
        this.fitness = fitness;

        shufflePath();
        initializeVectors();
        this.n = path.length;
    }

    public static void setLeader(Evaluable best) {
        AbstractSearchAgent.best = best;
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

    public void updateRoute(AbstractSearchAgent random) {
        if (this.getPath().hashCode() != best.getPath().hashCode()) {
            if (vectors.get(VectorDefinition.A).getAbsoluteValue() < 1) {
                for (int j = 0; j < path.length; j++) {
                    int k = getK(j);
                    int id = best.getPath().get(k);
                    for (int i = 0; i < path.length; i++) {
                        if (id == path[i].getId()) {
                            if (isDistanceShorterWithSwap(i, j)) {
                                Collections.swap(Arrays.asList(path), j, i);
                            }
                            break;
                        }
                    }
                }
            } else {
                for (int j = 0; j < path.length; j++) {
                    int k = getK(j);
                    int id = random.path[k].getId();
                    for (int i = 0; i < path.length; i++) {
                        if (id == path[i].getId()) {
                            if (isDistanceShorterWithSwap(i,j)) {
                                Collections.swap(Arrays.asList(path), j, i);
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    private double calcDistance(Node[] nodes, int index) {
        return calculateDistance(nodes, index, nodes[index]);
    }

    private double calcDistance(Node[] nodes, int index, int newNeighbor) {
        return calculateDistance(nodes, index, nodes[newNeighbor]);
    }

    private double calculateDistance(Node[] nodes, int index, Node node) {
        Node leftNeighbor;
        Node rightNeighbor;
        if (index == 0) {
            leftNeighbor = nodes[nodes.length - 1];
            rightNeighbor = nodes[index + 1];
        } else if (index == nodes.length - 1) {
            leftNeighbor = nodes[nodes.length - 2];
            rightNeighbor = nodes[0];
        } else {
            leftNeighbor = nodes[nodes.length - 2];
            rightNeighbor = nodes[index + 1];
        }

        return leftNeighbor.distance(node) + node.distance(rightNeighbor);
    }

    private boolean isDistanceShorterWithSwap(int i, int j) {
        return calcDistance(path, i) < calcDistance(path, i , j);

    }

    @Override
    public int hashCode() {
        return Objects.hash(new Object[]{this.path});
    }
}
