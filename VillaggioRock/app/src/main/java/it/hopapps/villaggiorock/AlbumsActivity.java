package it.hopapps.villaggiorock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.hopapps.villaggiorock.adapters.RVMenuAdapter;
import it.hopapps.villaggiorock.models.MenuItem;

public class AlbumsActivity extends AppCompatActivity {

    private List<MenuItem> albumsItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);
        initializeMenuItems();
        RecyclerView rv = (RecyclerView) findViewById(R.id.recicler_view_albums_menu);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        RVMenuAdapter adapter = new RVMenuAdapter(albumsItems, AlbumsActivity.this);
        rv.setAdapter(adapter);
        rv.setHasFixedSize(true);
    }

    private void initializeMenuItems(){
        albumsItems = new ArrayList<>();
        albumsItems.add(new MenuItem("Album 1", R.drawable.menu_item_events));
        albumsItems.add(new MenuItem("Album 2", R.drawable.menu_item_foto));
    }
}
