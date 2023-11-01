package woa;

import com.hsh.parser.Dataset;

import java.util.ArrayList;

public class WOA {

    private ArrayList<SearchAgent> searchAgents = new ArrayList<SearchAgent>();

    public WOA(int searchAgentPopulation, Dataset cities) {
        for(int i = 0; i < searchAgentPopulation; i++) {
            searchAgents.add(new SearchAgent(cities));
        }

    }



}
