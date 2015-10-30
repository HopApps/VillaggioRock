package it.hopapps.villaggiorock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import it.hopapps.villaggiorock.adapters.ImageGridAdapter;

public class AlbumPhotosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_photos);

        GridView gridview = (GridView) findViewById(R.id.album_photos_grid);
        gridview.setAdapter(new ImageGridAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(AlbumPhotosActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
