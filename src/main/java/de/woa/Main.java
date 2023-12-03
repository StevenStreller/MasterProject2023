package de.woa;


import com.hsh.Fitness;
import com.hsh.parser.Dataset;
import com.hsh.parser.Parser;
import de.woa.exceptions.DoubleInitializationNotPermittedException;
import de.woa.exceptions.RandomNotFoundException;
import de.woa.searchagent.AbstractSearchAgent;
import de.woa.searchagent.SearchAgentSet;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;


public class Main {

    private static int WHALE_POPULATION = 10;
    private static int TOTAL_ITERATION = 150;

    private static Fitness fitness;

    public static void main(String[] args) throws IOException, DoubleInitializationNotPermittedException, RandomNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (!(args.length >= 1)) {
            throw new IllegalArgumentException("Please set the path to the *.tsp");
        }
        if (args.length >= 2) {
            TOTAL_ITERATION = Integer.parseInt(args[1]);
        }
        if (args.length >= 3) {
            WHALE_POPULATION = Integer.parseInt(args[2]);
        }
        if (args.length == 1) {
            if (args[0].contains("generateHeuristic=")) {
                String pathToProblem = args[0].split("=")[1];
                generateIterationHeuristic(pathToProblem);
            }
        } else {
            runWhaleOptimizationAlgorithm(args[0]);
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
                TOTAL_ITERATION = iterations;
                for (int agents = 20; agents <= 100; agents += 20) {
                    WHALE_POPULATION = agents;
                    runWhaleOptimizationAlgorithm(directory.getPath() + "/" + Objects.requireNonNull(directory.list())[i]);

                    // A separate FileWriter for each iteration
                    FileWriter dataFileWriter = new FileWriter(file, true);
                    BufferedWriter bufferedWriter = new BufferedWriter(dataFileWriter);

                    int improvement = (fitness.getBest(0).getFitness() - fitness.getBest(TOTAL_ITERATION - 1).getFitness());
                    bufferedWriter.write(fitness.getDataset().getSize() + "," + TOTAL_ITERATION + "," + WHALE_POPULATION + ","
                            + improvement + "," + (improvement / TOTAL_ITERATION - 1));
                    bufferedWriter.newLine();
                    bufferedWriter.close();
                }
            }
        }

        fr.close();
    }

    private static void runWhaleOptimizationAlgorithm(String pathToData) throws IOException, DoubleInitializationNotPermittedException, RandomNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Dataset dataset = Parser.read(pathToData);

        // Initialization of the fitness class
        fitness = new Fitness(dataset);

        // Initializes whalePopulation many SearchAgents
        SearchAgentSet<? extends AbstractSearchAgent> searchAgentSet = getAgentSet(dataset, fitness);

        System.out.println("\n------------------["+ dataset.getType() +"]Whale Optimization Algorithm (WOA)------------------");
        System.out.println("Total iterations: " + TOTAL_ITERATION + " \\ " + "Whale Population: " + WHALE_POPULATION);
        System.out.println("Note: You can change the number of iterations and the whale population using the second <int> and third <int> argument.");

        for (int i = 0; i < TOTAL_ITERATION; i++) {
            if (i == 0) {
                searchAgentSet.setLeader(searchAgentSet.getRandom());
            }
            AbstractSearchAgent random = searchAgentSet.getRandom();
            for (AbstractSearchAgent searchAgent : searchAgentSet) {
                searchAgent.evaluate(i, TOTAL_ITERATION);
                searchAgent.updateRoute(random, searchAgentSet.getLeader()); // (3.1)
                fitness.evaluate(searchAgent, i);
            }
            searchAgentSet.setLeader(fitness.getBest(i));
        }
        fitness.finish();
    }

    private static SearchAgentSet<? extends AbstractSearchAgent> getAgentSet(Dataset dataset, Fitness fitness) throws DoubleInitializationNotPermittedException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        SearchAgentSet<? extends AbstractSearchAgent> searchAgentSet;
        if (dataset.getType().equals("TSP")) {
            searchAgentSet = new SearchAgentSet<>(de.woa.searchagent.tsp.SearchAgent.class, fitness, WHALE_POPULATION);
        } else if (dataset.getType().equals("SOP")){
            searchAgentSet = new SearchAgentSet<>(de.woa.searchagent.sop.SearchAgent.class, fitness, WHALE_POPULATION);
        } else {
            throw new IllegalArgumentException("The submitted file is neither *.tsp nor *.sop");
        }
        return searchAgentSet;
    }
}
