package model;

import model.entity.Sound;
import model.entity.Tag;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class BDDImporter {
    private List<Sound> sounds;
    private List<Tag> tags;

    public BDDImporter(List<Sound> sounds, List<Tag> tags) {
        this.sounds = sounds;
        this.tags = tags;
    }

    public void load() throws SQLException {
        Connection con = HehoConnection.getConnection();

        Statement stmt = con.createStatement();
        ResultSet soundsResultSet = stmt.executeQuery("SELECT * FROM heho_bd.sounds");
        while(soundsResultSet.next()) {
            sounds.add(new Sound(
                    soundsResultSet.getInt("sound_id"),
                    soundsResultSet.getString("name"),
                    soundsResultSet.getString("directory")
            ));
        }
        stmt.close();

        stmt = con.createStatement();
        ResultSet tagsResultSet = stmt.executeQuery("SELECT * FROM heho_bd.tags");
        while(tagsResultSet.next()) {
            tags.add(new Tag(
                    tagsResultSet.getInt("tag_id"),
                    tagsResultSet.getString("name"),
                    tagsResultSet.getInt("parent")
            ));
        }
        stmt.close();


        // Relations inside of tags
        tags.stream().filter(tag -> tag.getParentId() > 0).forEach(childTag -> {
            Tag parent = tags.stream().filter(parentTag -> childTag.getParentId() == parentTag.getId()).findFirst().get();
            childTag.setParent(parent);
        });

        stmt = con.createStatement();
        ResultSet relResultSet = stmt.executeQuery("SELECT * FROM heho_bd.sound_tag_rel");
        while(relResultSet.next()) {
            int tag_id   = relResultSet.getInt("tag_id");
            int sound_id = relResultSet.getInt("sound_id");

            sounds.stream().filter(sound -> sound.getId() == sound_id).findFirst().ifPresent(
                    sound -> sound.addTagGivenIdAndList(tags, tag_id)
            );
        }
        stmt.close();

        con.close();
    }

    public List<Sound> getSounds() {
        return sounds;
    }

    public void setSounds(List<Sound> sounds) {
        this.sounds = sounds;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
