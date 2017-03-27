package view_controller.customControls.listView;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import model.BDDImporter;
import model.entity.Sound;
import model.entity.Tag;
import view_controller.Environement;
import view_controller.StatusController;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CustomData {
    @FXML
    private HBox hBox;
    @FXML
    private Label name;
    @FXML
    private ImageView image;

    Sound sound;



    public CustomData() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("listCellItem.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setInfo(Sound sound) {
        this.sound = sound;
        name.setText(sound.getName());
    }

    @FXML protected void play() {
        StatusController.startTask(new Task() {
            @Override
            protected Void call() throws Exception {
                updateTitle("Playing " + sound.getName());
                updateMessage("Playing " + sound.getName());

                Environement environement = Environement.getInstance();

                try {
                    Media media = new Media(new File(
                            environement.getBaseDir() + "/" + sound.getDirectory()
                    ).toURI().toString());
                    MediaPlayer mediaPlayer = new MediaPlayer(media);
                    mediaPlayer.play();

                    image.setImage(new Image(getClass().getResource("../../../ressources/pause.png").toExternalForm()));

                    updateProgress(1,1);
                    done();

                } catch (Exception e) {
                    updateMessage("Failed to read media");
                    failed();
                }


                return null;
            }
        });

    }

    public HBox getBox()
    {
        return hBox;
    }
}
