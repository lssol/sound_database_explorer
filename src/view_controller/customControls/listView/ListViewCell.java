package view_controller.customControls.listView;

import javafx.scene.control.ListCell;
import model.entity.Sound;

public class ListViewCell extends ListCell<Sound> {
    @Override
    public void updateItem(Sound sound, boolean empty)
    {
        super.updateItem(sound, empty);
        if(sound != null && !empty)
        {
            CustomData data = new CustomData();
            data.setInfo(sound);
            setGraphic(data.getBox());
        } else {
            setGraphic(null);
        }
    }
}
