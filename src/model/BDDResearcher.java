package model;

import model.entity.Sound;
import model.entity.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BDDResearcher {
    private List<Sound> lastSearchResults;
    private BDDImporter importer;

    public BDDResearcher(BDDImporter importer) {
        this.importer = importer;
    }

    // TODO Make it optimized
    public List<Sound> search(List<String> tagNames) {
        List<Sound> sounds = importer.getSounds();

        if (!tagNames.isEmpty()) {
            List<Sound> soundsSelected = new ArrayList<>();

            for (Sound sound : sounds) {
                List<String> tagsFromSound = sound.getAllTagsWithParents().stream().map(Tag::getName).collect(Collectors.toList());

                if (tagsFromSound.containsAll(tagNames)) {
                   soundsSelected.add(sound);
                }
            }
            lastSearchResults = soundsSelected;
            return soundsSelected;
        } else {
            lastSearchResults = sounds;
            return sounds;
        }
    }
}
