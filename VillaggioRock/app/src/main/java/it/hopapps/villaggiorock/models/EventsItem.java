package it.hopapps.villaggiorock.models;

public class EventsItem {
    private String name;
    private String photoUrl;

    public EventsItem (String name, String photoUrl) {
        this.name = name;
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
}

