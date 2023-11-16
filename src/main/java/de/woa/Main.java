package de.woa;

import com.hsh.Fitness;
import com.hsh.parser.Dataset;
import com.hsh.parser.Parser;
import de.woa.exceptions.DoubleInitializationNotPermittedException;
import de.woa.exceptions.LeaderNotFoundException;
import de.woa.exceptions.RandomNotFoundException;

import java.io.IOException;


public class Main {

    private static final int WHALE_POPULATION = 10;
    private static final int TOTAL_ITERATION = 200;
    private static int currentIteration = 1;

    public static void main(String[] args) throws IOException, DoubleInitializationNotPermittedException, LeaderNotFoundException, RandomNotFoundException {
        if (args.length != 1) {
            throw new IllegalArgumentException("Please set the path to the *.tsp");
        } else {
            runWhaleOptimizationAlgorithm(args[0], WHALE_POPULATION);
        }
    }

    public static void runWhaleOptimizationAlgorithm(String pathToData, int whalePopulation) throws IOException, DoubleInitializationNotPermittedException, LeaderNotFoundException, RandomNotFoundException {
        System.out.println("\n------------------Whale Optimization Algorithm (WOA)------------------");
        Dataset dataset = Parser.read(pathToData);
        // Initialization of the fitness class
        Fitness fitness = new Fitness(dataset);
        // Initializes whalePopulation many SearchAgents
        SearchAgent.initializeSearchAgents(fitness, whalePopulation);
        SearchAgent leader = null;
        for (SearchAgent searchAgent : SearchAgent.getSearchAgents()) {
            System.out.println(searchAgent.getPath());
        }
        System.out.println("-----------------------");
        while (currentIteration < TOTAL_ITERATION) {
            leader = SearchAgent.getLeader(); // Kann vielleicht weg, weil macht ja nichts :-)
            for (SearchAgent searchAgent : SearchAgent.getSearchAgents()) {

                searchAgent.evaluate(currentIteration, TOTAL_ITERATION, leader);
                searchAgent.updateRoute(); // (3.1)

                fitness.evaluate(searchAgent, currentIteration);

            }
            //TODO: Update the current agent with the newly generated agent
            currentIteration++;
        }

        for (SearchAgent searchAgent : SearchAgent.getSearchAgents()) {
//            System.out.println(searchAgent.getPath());
//            System.out.println(searchAgent.getFitness());
        }

        fitness.finish();
    }
}
