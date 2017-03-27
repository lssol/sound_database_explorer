package view_controller;

import model.entity.Sound;
import model.entity.Tag;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

public class FileVisitorThatFillsModel implements FileVisitor<Path> {
    private Path base;
    private List<Sound> sounds;
    private Tags tags;

    class Tags extends ArrayList<Tag> {
        Tag addIfDoesntExist(Tag addedTag) {
            Optional<Tag> tagFound = this.stream()
                    .filter(tag -> Objects.equals(addedTag.getName(), tag.getName()) && addedTag.getParent() == tag.getParent())
                    .findFirst();
            if (tagFound.isPresent()) {
                return tagFound.get();
            } else {
                this.add(addedTag);
                return addedTag;
            }
        }
    }
    FileVisitorThatFillsModel(Path base) {
        this.base  = base;
        sounds     = new ArrayList<>();
        tags       = new Tags();
    }

    Path getBase() {
        return base;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Path relativePath = base.relativize(file);
        Tag currentTag = null;

        System.out.println("***** Visiting " + relativePath.toString());

        int nameCount = relativePath.getNameCount()-1;
        for(int i = 0; i <= nameCount; i++) {
            if (i == nameCount) {
                // We are on the name of the file
                sounds.add(new Sound(relativePath.getName(i).toString(), currentTag, relativePath.toString()));
                System.out.println("Added Sound " + relativePath.getName(i).toString());
                System.out.println("With parent " + currentTag.getName());

            } else {
                currentTag = tags.addIfDoesntExist(new Tag(relativePath.getName(i).toString(), currentTag));
                System.out.println("Added Tag " + relativePath.getName(i).toString());
            }
        }

        System.out.println("****** End of visit ");
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        if (dir.compareTo(base) == 0) {
            System.out.println("*-*-*-* Reorganizing the tags");
            // On est à la fin de la visite
            // On réorganise les tags (On utilise un clones car on supprime des éléments de tags dans la loop)
            Tags clonedTags = (Tags) tags.clone();
            for (Tag currentTag : clonedTags) {
                // On vérifie que le tags de la liste clonée actuelle n'a pas été supprimé de la vrai liste
                if (tags.stream().anyMatch(tag -> currentTag == tag)) {
                    List<Tag> similarTags = tags.stream().filter(tag -> Objects.equals(currentTag.getName(), tag.getName())).collect(Collectors.toList());
                    if (similarTags.size() > 1) {
                        Tag similatTagWeKeep = similarTags.get(0);
                        // If the tag was directly linked to a sound, the parent should be linked to it now and then we kill the parent
                        for (Tag similarTag : similarTags) {
                            System.out.println("--- Dealing with tag : " + similarTag.getName());
                            sounds.stream()
                                    .filter(sound -> sound.getTags().stream().anyMatch(tag -> tag == similarTag))
                                    .forEach(sound -> {
                                        sound.getTags().remove(similarTag);
                                        sound.getTags().add(similatTagWeKeep);
                                        sound.getTags().add(similarTag.getParent());
                                    });
                        }

                        boolean firstTimeInLoop = true;
                        for (Tag similarTag : similarTags) {
                            similarTag.setParent(null);
                            if (!firstTimeInLoop) tags.remove(similarTag);
                            else firstTimeInLoop = false;
                        }
                    }
                }
            }
        }
        return FileVisitResult.CONTINUE;
    }

    public List<Sound> getSounds() {
        return sounds;
    }

    public Tags getTags() {
        return tags;
    }
}
