package de.woa;

import com.hsh.Evaluable;
import com.hsh.Fitness;
import de.woa.exceptions.DoubleInitializationNotPermittedException;
import de.woa.exceptions.RandomNotFoundException;

import java.util.ArrayList;
import java.util.Random;

public class SearchAgentSet extends ArrayList<SearchAgent> {

    public SearchAgentSet(Fitness fitness, int population) throws DoubleInitializationNotPermittedException {
        if (!this.isEmpty()) {
            throw new DoubleInitializationNotPermittedException();
        } else {
            for (int i = 0; i < population; i++) {
                this.add(new SearchAgent(fitness));
            }
        }
    }

    public SearchAgent getRandom() throws RandomNotFoundException {
        SearchAgent random = this.get(new Random().nextInt(this.size()));
        if (random == null) {
            throw new RandomNotFoundException();
        }
        return random;
    }

}
