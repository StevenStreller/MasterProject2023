package de.woa.searchagent.sop;


import com.hsh.Fitness;
import com.hsh.parser.Node;
import de.woa.searchagent.AbstractSearchAgent;

import java.util.ArrayList;
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
}
