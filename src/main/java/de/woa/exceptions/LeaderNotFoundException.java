package de.woa.exceptions;

public class LeaderNotFoundException extends Exception{
    public LeaderNotFoundException() {
        super("The leader could not be found. This may be because the ArrayList<SearchAgent> searchAgents is empty.");
    }
}
