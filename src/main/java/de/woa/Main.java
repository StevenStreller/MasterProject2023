package de.woa;

import com.hsh.Evaluable;
import com.hsh.Fitness;
import com.hsh.parser.Dataset;
import com.hsh.parser.Parser;
import de.woa.exceptions.DoubleInitializationNotPermittedException;
import de.woa.exceptions.LeaderNotFoundException;
import de.woa.exceptions.RandomNotFoundException;

import java.io.IOException;


public class Main {

    private static int WHALE_POPULATION = 10;
    private static int TOTAL_ITERATION = 150;

    public static void main(String[] args) throws IOException, DoubleInitializationNotPermittedException, LeaderNotFoundException, RandomNotFoundException {
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

    public static void runWhaleOptimizationAlgorithm(String pathToData) throws IOException, DoubleInitializationNotPermittedException, LeaderNotFoundException, RandomNotFoundException {
        System.out.println("\n------------------Whale Optimization Algorithm (WOA)------------------");
        System.out.println("Total iterations: " + TOTAL_ITERATION + " \\ " + "Whale Population: " + WHALE_POPULATION);
        System.out.println("Note: You can change the number of iterations and the whale population using the second <int> and third <int> argument.");
        Dataset dataset = Parser.read(pathToData);
        // Initialization of the fitness class
        Fitness fitness = new Fitness(dataset);
        // Initializes whalePopulation many SearchAgents
        SearchAgent.initializeSearchAgents(fitness, WHALE_POPULATION);
        SearchAgent leader;
        for (SearchAgent searchAgent : SearchAgent.getSearchAgents()) {
            System.out.println(searchAgent.getPath());
        }
        System.out.println("-----------------------");
        for (int i = 0; i < TOTAL_ITERATION; i++) {
            if (i == 0) {
                SearchAgent.setLeader(SearchAgent.getRandom()) ;
            }

            for (SearchAgent searchAgent : SearchAgent.getSearchAgents()) {
                searchAgent.evaluate(i, TOTAL_ITERATION);
                searchAgent.updateRoute(); // (3.1)
                fitness.evaluate(searchAgent, i);
            }
            SearchAgent.setLeader(fitness.getBest(i));
        }

        fitness.finish();
    }
}
