package de.woa.searchagent.tsp;

import com.hsh.Fitness;
import de.woa.searchagent.AbstractSearchAgent;

import java.util.Arrays;
import java.util.Collections;

public class SearchAgent extends AbstractSearchAgent {
    public SearchAgent(Fitness fitness) {
        super(fitness);
    }

    @Override
    protected void shufflePath() {
        this.path = fitness.getDataset().getNodes();
        Collections.shuffle(Arrays.asList(path));
    }

    @Override
    protected boolean isDistanceShorterWithSwapAndConstraintCompliant(int i, int j) {
        return calcDistance(path, i) < calcDistance(path, i, j);
    }

}
