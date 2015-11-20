package it.hopapps.villaggiorock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import it.hopapps.villaggiorock.asyncTasks.FacebookAlbumPhotosRetriever;

public class AlbumPhotosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_photos);

        Intent i = this.getIntent();
        String eventId = i.getExtras().getString("id");

        GridView gridview = (GridView) findViewById(R.id.album_photos_grid);
        FacebookAlbumPhotosRetriever fbapr = new FacebookAlbumPhotosRetriever(eventId, this, gridview);
        fbapr.execute();
    }
}
