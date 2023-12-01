package de.woa;


import com.hsh.Fitness;
import com.hsh.parser.Dataset;
import com.hsh.parser.Parser;
import de.woa.exceptions.DoubleInitializationNotPermittedException;
import de.woa.exceptions.RandomNotFoundException;
import de.woa.searchagent.AbstractSearchAgent;
import de.woa.searchagent.SearchAgentSet;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;


public class Main {

    private static int WHALE_POPULATION = 10;
    private static int TOTAL_ITERATION = 200;

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
        runWhaleOptimizationAlgorithm(args[0]);
    }

    public static void runWhaleOptimizationAlgorithm(String pathToData) throws IOException, DoubleInitializationNotPermittedException, RandomNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Dataset dataset = Parser.read(pathToData);
        // Initialization of the fitness class
        Fitness fitness = new Fitness(dataset);

        // Initializes whalePopulation many SearchAgents
        SearchAgentSet<? extends AbstractSearchAgent> searchAgentSet;
        if (dataset.getType().equals("TSP")) {
            searchAgentSet = new SearchAgentSet<>(de.woa.searchagent.tsp.SearchAgent.class, fitness, WHALE_POPULATION);
        } else if (dataset.getType().equals("SOP")){
            searchAgentSet = new SearchAgentSet<>(de.woa.searchagent.sop.SearchAgent.class, fitness, WHALE_POPULATION);
        } else {
            throw new IllegalArgumentException("The submitted file is neither *.tsp nor *.sop");
        }

        System.out.println("\n------------------["+ dataset.getType() +"]Whale Optimization Algorithm (WOA)------------------");
        System.out.println("Total iterations: " + TOTAL_ITERATION + " \\ " + "Whale Population: " + WHALE_POPULATION);
        System.out.println("Note: You can change the number of iterations and the whale population using the second <int> and third <int> argument.");

        for (int i = 0; i < TOTAL_ITERATION; i++) {
            if (i == 0) {
                AbstractSearchAgent.setLeader(searchAgentSet.getRandom()) ;
            }
            AbstractSearchAgent random = searchAgentSet.getRandom();
            for (AbstractSearchAgent searchAgent : searchAgentSet) {
                searchAgent.evaluate(i, TOTAL_ITERATION);
                searchAgent.updateRoute(random); // (3.1)
                fitness.evaluate(searchAgent, i);
            }
            AbstractSearchAgent.setLeader(fitness.getBest(i));
        }
        fitness.finish();
    }
}
