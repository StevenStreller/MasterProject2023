package de.woa.searchagent;

import com.hsh.Fitness;

import java.util.Arrays;
import java.util.Collections;

public class TspAgent extends AbstractSearchAgent {
    public TspAgent(Fitness fitness) {
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
