package it.hopapps.villaggiorock.models;

public class EventsItem {
    private String id;
    private String name;
    private String photoUrl;

    public EventsItem (String id, String name, String photoUrl) {
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

