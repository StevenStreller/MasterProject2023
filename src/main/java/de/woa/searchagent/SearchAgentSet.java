package de.woa.searchagent;

import com.hsh.Evaluable;
import com.hsh.Fitness;
import de.woa.exceptions.DoubleInitializationNotPermittedException;
import de.woa.exceptions.RandomNotFoundException;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;


public class SearchAgentSet<T extends AbstractSearchAgent> extends ArrayList<T> {

    private Evaluable leader;

    public SearchAgentSet(Class<T> searchAgentClass, Fitness fitness, int population) throws DoubleInitializationNotPermittedException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (!this.isEmpty()) {
            throw new DoubleInitializationNotPermittedException();
        } else {
            for (int i = 0; i < population; i++) {
                T searchAgent = searchAgentClass.getDeclaredConstructor(Fitness.class).newInstance(fitness);
                this.add(searchAgent);
            }
        }
    }

    public T getRandom() throws RandomNotFoundException {
        T random = this.get(new Random().nextInt(this.size()));
        if (random == null) {
            throw new RandomNotFoundException();
        }
        return random;
    }

    public void setLeader(Evaluable evaluable) {
        this.leader = evaluable;
    }


    public Evaluable getLeader() {
        return leader;
    }
}
