package it.univaq.pathofdungeons.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.util.Date;

/**
 * Implementation of {@link Logger} that logs to a file in the current directory 
 */
public class FileLogger implements Logger{
    //Setup for file logging, creates the logs folder and the file for the current session
    {
        Path root = Path.of(".").toAbsolutePath().normalize();
        try{
            root = Files.exists(root.resolve("logs")) ? root.resolve("logs") : Files.createDirectories(root.resolve("logs"));
        }
        catch(IOException e){
            System.err.println("Could not create logs folder, logging will be done in " + root + " instead");
        }
        try{
            Path file = Files.createFile(root.resolve(DateFormat.getDateTimeInstance().format(new Date())+".txt"));
            System.out.println("logging in " + file);
            bw = new BufferedWriter(new FileWriter(file.toString()));
        } catch(IOException e){
            System.err.println("Could not create a log file, logging will only be done in the console");
        }
    }

    private static FileLogger fl = new FileLogger();

    private static BufferedWriter bw;

    private FileLogger(){}

    public static Logger getInstance(){return fl; }

    @Override
    public void info(String msg) {
        String message = String.format("[%s][INFO] %s", DateFormat.getDateTimeInstance().format(new Date()), msg);
        System.out.println(message);
        this.writeToFile(message);
    }

    @Override
    public void error(String msg) {
        String message = String.format("[%s][ERROR] %s", DateFormat.getDateTimeInstance().format(new Date()), msg);
        System.err.println(message);
        this.writeToFile(message);
    }

    @Override
    public void debug(String msg) {
        String message = String.format("[%s][DEBUG] %s", DateFormat.getDateTimeInstance().format(new Date()), msg);
        System.out.println(message);
        this.writeToFile(message);
    }

    private void writeToFile(String msg){
        try{
            bw.append(msg);
            bw.append('\n');
            bw.flush();
        } catch(IOException e){
            System.err.println("Could not write log to file");
        } catch(NullPointerException e){
            //This means we cannot write to the file as the initial BufferedWriter was not created, just ignore write instructions
        }
    }

}