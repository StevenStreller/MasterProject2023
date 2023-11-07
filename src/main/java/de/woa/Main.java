package de.woa;

import com.hsh.Fitness;
import com.hsh.parser.Dataset;
import com.hsh.parser.Parser;
import de.woa.enums.Route;
import de.woa.exceptions.DoubleInitializationNotPermittedException;
import de.woa.exceptions.LeaderNotFoundException;

import java.io.IOException;

public class Main {

    private static final int WHALE_POPULATION = 10;
    private static final int TOTAL_ITERATION = 1000;
    private static int currentIteration = 0;

    public static void main(String[] args) throws IOException, DoubleInitializationNotPermittedException, LeaderNotFoundException {
        if (args.length != 1) {
            throw new IllegalArgumentException("Please set the path to the *.tsp");
        } else {
            runWhaleOptimizationAlgorithm(args[0], WHALE_POPULATION);
        }
    }

    public static void runWhaleOptimizationAlgorithm(String pathToData, int whalePopulation) throws IOException, DoubleInitializationNotPermittedException, LeaderNotFoundException {
        System.out.println("\n------------------Whale Optimization Algorithm (WOA)------------------");
        Dataset dataset = Parser.read(pathToData);
        // Initialization of the fitness class
        Fitness fitness = new Fitness(dataset);

        // Initializes whalePopulation many SearchAgents
        SearchAgent.initializeSearchAgents(fitness, whalePopulation);
        SearchAgent leader;
        while (currentIteration <= TOTAL_ITERATION) {
            leader = SearchAgent.getLeader();
            for (SearchAgent searchAgent : SearchAgent.getSearchAgents()) {
                // Evaluate a, A, C, l and p
                if (searchAgent.getP() < 0.5) {
                    if (searchAgent.getA() < 1) {
                        searchAgent.getK(Route.LEADER); // (3.3)
                        searchAgent.updateRoute(Route.LEADER); // (3.1)
                    } else {
                        searchAgent.getK(Route.RANDOM); // (3.2)
                        searchAgent.updateRoute(Route.LEADER); // (3.1)
                    }
                } else {
                    searchAgent.getK(Route.LEADER); // (3.2)
                    searchAgent.updateRoute(Route.RANDOM); // (3.1)
                }

            }
            //TODO: Update the current agent with the newly generated agent
            currentIteration++;
        }

    }
}
