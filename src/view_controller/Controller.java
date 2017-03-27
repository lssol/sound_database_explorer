package view_controller;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.BDDImporter;
import model.BDDSynchroniser;
import model.HehoConnection;
import model.entity.Sound;
import model.entity.Tag;
import view_controller.customControls.TagBar;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Controller {
    public TagBar tagBar;
    public VBox centerBox;

    @FXML
    BorderPane root;

    private List<Sound> sounds;
    private List<Tag> tags;

    private Environement environement = Environement.getInstance();

    @FXML
    protected void initialize() {
        StatusController.initialize(root);
        StatusController.startTask(new Task() {
            @Override
            protected Void call() throws Exception {
                updateTitle("Connecting to database");
                try {
                    updateMessage("Trying to connect to database...");
                    Connection connection = HehoConnection.getConnection();
                    updateMessage("Connected successfully");
                    connection.close();
                    updateMessage("Connection closed");
                } catch (SQLException e) {
                    updateMessage("Failed to connect to database");
                    e.printStackTrace();
                    throw e;
                }
                updateProgress(1,1);
                done();
                return null;
            }
        });
        try {
            centerBox.getChildren().add(0,FXMLLoader.load(getClass().getResource("customControls/listView/listView.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML protected void analyseDirectory(ActionEvent event) {
        StatusController.startTask(new Task() {
            @Override
            protected Void call() throws Exception {
                updateTitle("Analysing directories");
                updateMessage("Analysing directories");

                String dirToAnalyse = environement.getBaseDir();
                FileVisitorThatFillsModel fileVisitor = new FileVisitorThatFillsModel(Paths.get(dirToAnalyse));
                try {
                    Files.walkFileTree(fileVisitor.getBase(), fileVisitor);
                    tags   = fileVisitor.getTags();
                    sounds = fileVisitor.getSounds();
                } catch (Exception e) {
                    updateMessage("Failed to analyse directories");
                    e.printStackTrace();
                    throw e;
                }

                updateProgress(1,1);
                done();
                return null;
            }
        });
    }

    @FXML protected void synchronizeDatabase(ActionEvent event) {
        StatusController.startTask(new Task() {
            @Override
            protected Void call() throws Exception {
                updateTitle("Synchronise with DB");
                updateMessage("Synchronising with DB");

                BDDSynchroniser synchroniser = new BDDSynchroniser();
                synchroniser.setSounds(sounds);
                synchroniser.setTags(tags);
                try {
                    synchroniser.synchronize();
                } catch (Exception e) {
                    updateMessage("Failed to synchronise with DB");
                    e.printStackTrace();
                    throw e;
                }

                updateProgress(1, 1);
                done();
                return null;
            }
        });
    }

    @FXML protected void loadDatabase(ActionEvent event) {
        StatusController.startTask(new Task() {
            @Override
            protected Void call() throws Exception {
                updateTitle("Loading DB");
                updateMessage("Loading DB");

                BDDImporter loader = new BDDImporter(sounds, tags);

                try {
                    loader.load();
                } catch (Exception e) {
                    updateMessage("Failed to get information from DB");
                    e.printStackTrace();
                    throw e;
                }

                updateProgress(1,1);
                done();
                return null;
            }
        });
    }
}
