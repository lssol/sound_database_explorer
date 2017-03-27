package model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Sound {
    private  int id;
    private  String name;
    private  List<Tag> tags = new ArrayList<>();
    private String directory;

    public Sound(int id, String name, String directory) {
        this.name = name;
        this.directory = directory;
        this.id = id;
    }

    public Sound(String name, Tag tag, String directory) {
        this.name = name;
        if (tag != null) {
            tags.add(tag);
        }
        this.directory = directory;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void addTagGivenIdAndList(List<Tag> tags, int id) {
        Optional<Tag> tagToAdd = tags.stream().filter(tag -> tag.getId() == id).findFirst();
        tagToAdd.ifPresent(tag -> this.tags.add(tag));
    }

    public List<Tag> getAllTagsWithParents() {
        List<Tag> result = new ArrayList<Tag>();
        for (Tag tag : this.getTags()) {
            result.addAll(tag.getAllTagsAssociated());
        }

        return result;
    }
}
