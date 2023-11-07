package de.woa.exceptions;

public class RandomNotFoundException extends Exception{
    public RandomNotFoundException() {
        super("The random could not be found. This may be because the ArrayList<SearchAgent> searchAgents is empty.");
    }
}
