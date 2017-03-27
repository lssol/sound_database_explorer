package view_controller;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.StatusBar;

import java.util.LinkedHashMap;

public class StatusController {
    private static LinkedHashMap<String, Boolean> history = new LinkedHashMap<>();
    private static StatusBar statusBar;

    static void initialize(BorderPane root) {
        statusBar = new StatusBar();
        statusBar.setText("Programme démarré avec succés");

        root.setBottom(statusBar);
    }

    public static void startTask(Task task) {
        task.setOnSucceeded(event -> history.put(task.getTitle(), true));
        task.setOnFailed(event -> history.put(task.getTitle(), false));

        statusBar.textProperty().bind(task.messageProperty());
        statusBar.progressProperty().bind(task.progressProperty());

        Thread thread = new Thread(task);
        thread.start();
    }
}
