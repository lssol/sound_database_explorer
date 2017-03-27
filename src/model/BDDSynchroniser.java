package model;

import model.entity.Sound;
import model.entity.Tag;

import java.sql.*;
import java.util.List;

public class BDDSynchroniser {
    private List<Sound> sounds;
    private List<Tag> tags;

    public void setSounds(List<Sound> sounds) {
        this.sounds = sounds;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void synchronize() throws SQLException {
        PreparedStatement stmt = null;
        Connection con = HehoConnection.getConnection();

        String disableFKCheck = "SET FOREIGN_KEY_CHECKS = 0;";
        String truncateRel = "TRUNCATE TABLE heho_bd.sound_tag_rel";
        String truncateSounds = "TRUNCATE TABLE heho_bd.sounds";
        String truncateTags = "TRUNCATE TABLE heho_bd.tags";
        String enableFKCheck = "SET FOREIGN_KEY_CHECKS = 1;";

        String updateSounds = "INSERT INTO heho_bd.sounds " +
                "(sound_id, directory, name) VALUES (?, ?, ?)";

        String updateTags = "INSERT INTO  heho_bd.tags " +
                "(tag_id, parent, name) VALUES (?, ?, ?)";

        String updateRel = "INSERT INTO heho_bd.sound_tag_rel " +
                "(tag_id, sound_id) VALUES (?, ?)";

        Statement killer = con.createStatement();
        killer.executeUpdate(disableFKCheck);
        killer.executeUpdate(truncateRel);
        killer.executeUpdate(truncateSounds);
        killer.executeUpdate(truncateTags);
        killer.executeUpdate(enableFKCheck);

        for(int i = 0; i < sounds.size(); i++) {
            sounds.get(i).setId(i+1);
        }

        for(int i = 0; i < tags.size(); i++) {
            tags.get(i).setId(i+1);
        }

        for(Tag tag : tags) {
            stmt = con.prepareStatement(updateTags);
            stmt.setInt(1, tag.getId());
            if (tag.getParent() != null) {
                stmt.setInt(2, tag.getParent().getId());
            } else {
                stmt.setNull(2, Types.BIGINT);
            }
            stmt.setString(3, tag.getName());
            stmt.executeUpdate();
        }

        for(Sound sound : sounds) {
            stmt = con.prepareStatement(updateSounds);
            stmt.setInt(1, sound.getId());
            stmt.setString(2, sound.getDirectory());
            stmt.setString(3, sound.getName());
            stmt.executeUpdate();
            for(Tag tag : sound.getTags()) {
                stmt = con.prepareStatement(updateRel);
                stmt.setInt(1, tag.getId());
                stmt.setInt(2, sound.getId());
                stmt.executeUpdate();
            }
        }
        stmt.close();
        con.close();
    }
}
