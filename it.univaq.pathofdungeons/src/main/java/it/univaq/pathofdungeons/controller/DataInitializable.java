package it.univaq.pathofdungeons.controller;

/**
 * Interface that should be used for view controllers, it contains
 * methods that should be executed when initializing said controller
 */
public interface DataInitializable<T> {
    /**
     * Initializes the controller using the data passed as a paramter
     * @param data data used to initialize the controller
     */
    void initialize(T data);
    /**
     * Default implementation for controllers that do not require data to be initialized
     */
    default void initialize(){}
    /**
     * Method that runs after the controller is initialized. Default implementation does nothing
     */
    default void postInitialize(){}
}
