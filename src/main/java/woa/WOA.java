package woa;

import com.hsh.parser.Dataset;

import java.util.ArrayList;

public class WOA {

    private int maxIter = 1;
    private int currentIter = 0;

    private Fitness fitness;

    private ArrayList<SearchAgent> searchAgents = new ArrayList<SearchAgent>();

    public WOA(int searchAgentPopulation, Fitness fitness) {
        this.fitness = fitness;
        // initialize random whale population
        for(int i = 0; i < searchAgentPopulation; i++) {
            searchAgents.add(new SearchAgent(fitness.getDataset(), maxIter));
//            searchAgents.get(i).calcValues(maxIter, currentIter);
        }

        // start of algorithm
        while (currentIter >= maxIter) {
            // TODO: Evaluate distance of each

            for (SearchAgent searchAgent : searchAgents) {
                // Find the best Route with min distance
                searchAgent.updatePosition(getLeader(), currentIter);
            }
            currentIter++;
        }
        System.out.println(getLeader().getP());
        System.out.println(fitness.evaluate(getLeader().getP(), currentIter));
    }



}
