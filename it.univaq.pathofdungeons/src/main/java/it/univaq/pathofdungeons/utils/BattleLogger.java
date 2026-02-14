package it.univaq.pathofdungeons.utils;

import java.text.DateFormat;
import java.util.Date;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * Implementation of {@link Logger} that updates the side log in the battle menu before logging to file using {@link FileLogger}
 */
public class BattleLogger implements Logger{
    private static Text flow;
    private static BattleLogger bl = new BattleLogger();

    private BattleLogger(){}

    public static void initPane(TextFlow f){ 
        flow = new Text();
        f.getChildren().add(flow);
    }

    public static Logger getInstance(){ return bl; }

    @Override
    public void info(String msg) {
        flow.setText(flow.getText().concat(String.format("[%s][INFO] %s%n", DateFormat.getTimeInstance().format(new Date()), msg)));
        FileLogger.getInstance().info(msg);
    }

    @Override
    public void error(String msg) {
        flow.setText(flow.getText().concat(String.format("[%s][ERROR] %s%n", DateFormat.getTimeInstance().format(new Date()), msg)));
        FileLogger.getInstance().error(msg);
    }

    @Override
    public void debug(String msg) {
        flow.setText(flow.getText().concat(String.format("[%s][DEBUG] %s%n", DateFormat.getTimeInstance().format(new Date()), msg)));
        FileLogger.getInstance().debug(msg);
    }
}
