package it.hopapps.villaggiorock.models;

public class CustomMenuItem {
    private String name;
    private int photoId;

    public CustomMenuItem(String name, int photoId) {
        this.name = name;
        this.photoId = photoId;
    }

    public String getName() {
        return name;
    }

    public int getPhotoId() {
        return photoId;
    }
}
