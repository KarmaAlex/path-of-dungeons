package it.univaq.pathofdungeons.controller;

public interface DataInitializable<T> {
    /**
     * Initializes the controller using the data passed as a paramter
     * @param data data used to initialize the controller
     */
    void initialize(T data);
    /**
     * Method that runs after the controller is initialized. Default implementation does nothing
     */
    default void postInitialize(){};
}
