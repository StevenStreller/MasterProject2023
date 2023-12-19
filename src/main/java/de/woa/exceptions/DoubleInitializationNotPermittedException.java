package de.woa.exceptions;

public class DoubleInitializationNotPermittedException extends Exception{
    public DoubleInitializationNotPermittedException() {
        super("The SearchAgents have already been initialized. Please only access the SearchAgents via the getSearchAgents() method.");
    }
}
