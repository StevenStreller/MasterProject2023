package woa;

import com.hsh.Evaluable;
import com.hsh.Fitness;
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

    private SearchAgent getLeader() {
        int minDistance = Integer.MAX_VALUE;
        int index = 0;
        for (int i = 0; i < searchAgents.size(); i++) {
            int distance = fitness.evaluate(searchAgents.get(i).getP(), currentIter).getFitness();
                if (distance < minDistance) {
                    minDistance = distance;
                    index = i;
                }
        }
        return searchAgents.get(index);
    }
}
