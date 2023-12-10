package de.woa.searchagent;


import com.hsh.Fitness;
import com.hsh.parser.Node;
import de.woa.searchagent.AbstractSearchAgent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class SopAgent extends AbstractSearchAgent {

    public SopAgent(Fitness fitness) {
        super(fitness);
    }

    @Override
    protected void shufflePath() {
        this.path = fitness.getDataset().getNodes();
        Collections.shuffle(Arrays.asList(path));
        while (!fitness.validate(Arrays.stream(path).mapToInt(Node::getId).toArray()).isValid()) {
            for (int i = 0; i < path.length; i++) {
                ArrayList<Integer> constraints = path[i].getConstraints();
                for (int cId : constraints) {
                    int cIndex = getIndexById(cId);
                    if (cIndex > i) {
                        Collections.swap(Arrays.asList(path), i, cIndex);
                    }
                }
            }
        }
    }

    @Override
    protected boolean isDistanceShorterWithSwapAndConstraintCompliant(int i, int j) {
        if (calcDistance(path, i) < calcDistance(path, i, j)) {
            int[] tempPath = Arrays.stream(path).mapToInt(Node::getId).toArray();
            int temp = tempPath[i];
            tempPath[i] = tempPath[j];
            tempPath[j] = temp;
            return fitness.validate(tempPath).isValid();
        }

        return false;
    }


}
