package view_controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.BDDImporter;

public class Environement {
    private static Environement ourInstance = new Environement();

    public static Environement getInstance() {
        return ourInstance;
    }

    private ObservableList observableList = FXCollections.observableArrayList();
    private BDDImporter importer;
    private String baseDir = "/home/brisset/IdeaProjects/HEHO/example";

    private Environement() {

    }

    public String getBaseDir() {
        return baseDir;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public BDDImporter getImporter() {
        return importer;
    }

    public void setImporter(BDDImporter importer) {
        this.importer = importer;
    }

    public ObservableList getObservableList() {
        return observableList;
    }
}
