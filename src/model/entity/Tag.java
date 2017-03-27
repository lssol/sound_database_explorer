package model.entity;

import java.util.ArrayList;
import java.util.List;

public class Tag {
    private Integer id = null;
    private String name;
    private Tag parent;
    private int parentId; // Needed in BDDImporter just as a temporary variable

    public Tag(int id, String name, int parentId) {
        this.name = name;
        this.id = id;
        this.parentId = parentId;
    }

    public Tag(String name, Tag parent) {
        this.name   = name;
        this.parent = parent;
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

    public Tag getParent() {
        return parent;
    }

    public void setParent(Tag parent) {
        this.parent = parent;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public List<Tag> getAllTagsAssociated() {
        return getAllTagsAssociatedRecursive(new ArrayList<Tag>(), this);
    }

    private List<Tag> getAllTagsAssociatedRecursive(List<Tag> list, Tag tag) {
        list.add(tag);
        if (tag.parent != null) {
            return getAllTagsAssociatedRecursive(list, tag.getParent());
        } else {
            return list;
        }
    }
}
