package view_controller.customControls.listView;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.BDDImporter;
import model.entity.Sound;
import model.entity.Tag;
import view_controller.Environement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ListViewController {
    @FXML
    private ListView listView;
    private BDDImporter importer;
    private List<Sound> sounds;
    private List<Tag> tags;

    Environement environment = Environement.getInstance();

    @FXML
    void initialize() {
        assert listView != null : "fx:id=\"listView\" was not injected: check your FXML file 'CustomList.fxml'.";
        setListView();
    }

    public void setListView() {
        sounds = new ArrayList<>();
        tags = new ArrayList<>();

        importer = new BDDImporter(sounds, tags);
        try {
            importer.load();
            environment.setImporter(importer);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        environment.getObservableList().setAll(sounds);
        listView.setItems(environment.getObservableList());
        listView.setCellFactory(listView -> new ListViewCell());
    }
}
