package de.woa;

import com.hsh.Evaluable;
import com.hsh.Fitness;
import com.hsh.parser.Dataset;
import com.hsh.parser.Parser;
import de.woa.exceptions.DoubleInitializationNotPermittedException;
import de.woa.exceptions.LeaderNotFoundException;
import de.woa.exceptions.RandomNotFoundException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    private static final int WHALE_POPULATION = 10;
    private static final int TOTAL_ITERATION = 100;
    private static int currentIteration = 0;

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

        while (currentIteration <= TOTAL_ITERATION) {
            ArrayList<Evaluable> evaluables = new ArrayList<>();
            leader = SearchAgent.getLeader(); // Kann vielleicht weg, weil macht ja nichts :-)
            for (SearchAgent searchAgent : SearchAgent.getSearchAgents()) {

                searchAgent.evaluate(currentIteration, TOTAL_ITERATION);
                searchAgent.getK();
                searchAgent.updateRoute(); // (3.1)

            }
            //TODO: Update the current agent with the newly generated agent
            currentIteration++;
            evaluables.add(leader);
            fitness.evaluate(evaluables);
        }
        fitness.finish();
    }
}
