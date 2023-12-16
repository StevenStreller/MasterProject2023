package de.woa;


import com.hsh.Evaluable;
import com.hsh.Fitness;
import com.hsh.parser.Dataset;
import com.hsh.parser.Parser;
import de.woa.exceptions.DoubleInitializationNotPermittedException;
import de.woa.exceptions.RandomNotFoundException;
import de.woa.searchagent.AbstractSearchAgent;
import de.woa.searchagent.SearchAgentSet;
import de.woa.searchagent.SopAgent;
import de.woa.searchagent.TspAgent;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;


public class Main {

    private static Fitness fitness;

    public static void main(String[] args) throws IOException, DoubleInitializationNotPermittedException, RandomNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (!(args.length >= 1)) {
            throw new IllegalArgumentException("Please set the path to the *.tsp");
        }

        String directoryPathToProblems = null;
        int totalIterations = -1;
        int whalePopulation = -1;
        boolean hasDynamicIteration = true;

        for (String arg : args) {
            if (arg.toLowerCase().matches("total_iterations=\\d+")) {
                totalIterations = Integer.parseInt(arg.toLowerCase().split("=")[1]);
            } else if (arg.toLowerCase().matches("whale_population=\\d+")) {
                whalePopulation = Integer.parseInt(arg.toLowerCase().split("=")[1]);
            } else if (arg.toLowerCase().matches("generate_heuristic=.+")) {
                directoryPathToProblems = arg.toLowerCase().split("=")[1];
            } else if (arg.toLowerCase().matches("dynamic_iterations=false")) {
                System.out.println("Hint: You have deactivated dynamic iteration.");
                hasDynamicIteration = false;
            }
        }

        if (directoryPathToProblems != null) {
            generateIterationHeuristic(directoryPathToProblems);
        } else {
            Dataset dataset = Parser.read(args[0]);
            int[] closestIteration = IterationCalculator.findClosest(dataset.getSize());
            assert closestIteration != null;
            System.out.println("We have found a similar problem in our knowledge base. We found that for a number of " + closestIteration[0] + " cities, the number of iterations "+ closestIteration[1] +" and the whale population of " + closestIteration[2] + " is the best.");

            if (totalIterations != -1) {
                closestIteration[1] = totalIterations;
                System.out.println("Hint: You have overwritten the original value from the knowledge base by setting the argument TOTAL_ITERATIONS.");
            }
            if (whalePopulation != -1) {
                closestIteration[2] = whalePopulation;
                System.out.println("Hint: You have overwritten the original value from the knowledge base by setting the argument WHALE_POPULATION.");
            }

            runWhaleOptimizationAlgorithm(dataset, closestIteration[1], closestIteration[2], hasDynamicIteration);
        }

    }

    private static void generateIterationHeuristic(String path) throws DoubleInitializationNotPermittedException, RandomNotFoundException, IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        File directory = new File(path);

        File file = new File("heuristic.csv");
        FileWriter fr = new FileWriter(file, true);
        BufferedWriter br = new BufferedWriter(fr);
        br.write("Started at " + System.currentTimeMillis() / 1000 + "\n");
        String header = "Total Nodes,Total iterations,Whale population,Improvement,Improvement/Total iterations\n";
        br.write(header);
        br.close();

        for (int i = 0; i < Objects.requireNonNull(directory.list()).length; i++) {
            for (int iterations = 200; iterations <= 1000; iterations += 100) {
                for (int agents = 20; agents <= 100; agents += 20) {
                    if (Parser.read(directory.getPath() + "/" + Objects.requireNonNull(directory.list())[i]).getSize() <= 1000) {
                        runWhaleOptimizationAlgorithm(Parser.read(directory.getPath() + "/" + Objects.requireNonNull(directory.list())[i]), iterations, agents, false);

                        // A separate FileWriter for each iteration
                        FileWriter dataFileWriter = new FileWriter(file, true);
                        BufferedWriter bufferedWriter = new BufferedWriter(dataFileWriter);

                        int improvement = (fitness.getBest(0).getFitness() - fitness.getBest(iterations - 1).getFitness());
                        bufferedWriter.write(fitness.getDataset().getSize() + "," + iterations + "," + agents + ","
                                + improvement + "," + (improvement / iterations - 1));
                        bufferedWriter.newLine();
                        bufferedWriter.close();
                    }

                }
            }
        }

        fr.close();
    }

    private static void runWhaleOptimizationAlgorithm(Dataset dataset, int totalIteration, int whalePopulation, boolean dynamicIteration) throws DoubleInitializationNotPermittedException, RandomNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        // Initialization of the fitness class
        fitness = new Fitness(dataset);
        int fifteenPercent = (int) (totalIteration * 0.15);

        // Initializes whalePopulation many SearchAgents
        SearchAgentSet<? extends AbstractSearchAgent> searchAgentSet = getAgentSet(dataset, fitness, whalePopulation);

        System.out.println("\n------------------[" + dataset.getType() + "] Whale Optimization Algorithm (WOA)------------------");
        System.out.println("Total iterations: " + totalIteration + " \\ " + "Whale Population: " + whalePopulation);

        for (int i = 0; i < totalIteration; i++) {
            if (i == 0) {
                searchAgentSet.setLeader(searchAgentSet.getRandom());
            }
            AbstractSearchAgent random = searchAgentSet.getRandom();
            Evaluable leader = searchAgentSet.getLeader();
            for (AbstractSearchAgent searchAgent : searchAgentSet) {
                searchAgent.evaluate(i, totalIteration, leader);
                searchAgent.updateRoute(random, leader); // (3.1)
                fitness.evaluate(searchAgent, i);
            }
            searchAgentSet.setLeader(fitness.getBest(i));

            if (dynamicIteration) {
                if (i == totalIteration -1) {
                    int j = totalIteration - fifteenPercent;
                    int best = fitness.getBest(j).getFitness();
                    for (; j < totalIteration; j++) {
                        if (best > fitness.getBest(j).getFitness()) {
                            totalIteration += fifteenPercent;
                            break;
                        }
                    }
                }
            }
        }
        fitness.finish();
    }

    private static SearchAgentSet<? extends AbstractSearchAgent> getAgentSet(Dataset dataset, Fitness fitness, int whalePopulation) throws DoubleInitializationNotPermittedException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        SearchAgentSet<? extends AbstractSearchAgent> searchAgentSet;
        if (dataset.getType().equals("TSP")) {
            searchAgentSet = new SearchAgentSet<>(TspAgent.class, fitness, whalePopulation);
        } else if (dataset.getType().equals("SOP")) {
            searchAgentSet = new SearchAgentSet<>(SopAgent.class, fitness, whalePopulation);
        } else {
            throw new IllegalArgumentException("The submitted file is neither *.tsp nor *.sop");
        }
        return searchAgentSet;
    }
}
