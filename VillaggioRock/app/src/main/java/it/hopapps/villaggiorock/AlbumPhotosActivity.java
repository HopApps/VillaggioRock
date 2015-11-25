package it.hopapps.villaggiorock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import it.hopapps.villaggiorock.asyncTasks.FacebookAlbumPhotosRetriever;

public class AlbumPhotosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_photos);

        GridView gridView = (GridView) findViewById(R.id.album_photos_grid);
        String eventId = this.getIntent().getExtras().getString("id");

        new FacebookAlbumPhotosRetriever(eventId, this, gridView).execute();
    }
}
