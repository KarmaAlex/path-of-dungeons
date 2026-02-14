package it.univaq.pathofdungeons.utils;

public interface Logger {
    /**
     * Log an info string, adding a timestamp at the start
     * @param msg string to log
     */
    public void info(String msg);
    /**
     * Log an error string, adding a timestamp at the start
     * @param msg string to log
     */
    public void error(String msg);
    /**
     * Log a debug string, adding a timestamp at the start
     * @param msg string to log
     */
    public void debug(String msg);
}
