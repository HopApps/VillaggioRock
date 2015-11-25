package it.hopapps.villaggiorock.models;

public class AlbumsItem {
    private String id;
    private String name;
    private String photoUrl;

    public AlbumsItem (String id, String name, String photoUrl) {
        this.id = id;
        this.name = name;
        this.photoUrl = photoUrl;
    }

    public String getId() { return id; }
    public String getName() {
        return name;
    }
    public String getPhotoUrl() {
        return photoUrl;
    }

}
