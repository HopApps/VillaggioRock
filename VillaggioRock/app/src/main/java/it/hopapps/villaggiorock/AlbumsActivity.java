package it.hopapps.villaggiorock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import it.hopapps.villaggiorock.asyncTasks.FacebookAlbumsRetriever;

public class AlbumsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);
        GridView gridview = (GridView) findViewById(R.id.albums_grid_view);
        FacebookAlbumsRetriever fbev = new FacebookAlbumsRetriever(this, gridview);
        fbev.execute();
    }
}
