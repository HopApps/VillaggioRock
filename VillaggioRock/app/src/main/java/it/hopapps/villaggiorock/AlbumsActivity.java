package it.hopapps.villaggiorock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import it.hopapps.villaggiorock.adapters.AlbumsImageAdapter;
import it.hopapps.villaggiorock.adapters.RVMenuAdapter;
import it.hopapps.villaggiorock.models.MenuItem;

public class AlbumsActivity extends AppCompatActivity {

    private List<MenuItem> albumsItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);
        initializeMenuItems();
        GridView gridview = (GridView) findViewById(R.id.albums_grid_view);
        gridview.setAdapter(new AlbumsImageAdapter(this, albumsItems));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(AlbumsActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeMenuItems(){
        albumsItems = new ArrayList<>();
        albumsItems.add(new MenuItem("Album 1", R.drawable.menu_item_events));
        albumsItems.add(new MenuItem("Album 2", R.drawable.menu_item_foto));
        albumsItems.add(new MenuItem("Album 3", R.drawable.menu_item_gadget));
        albumsItems.add(new MenuItem("Album 4", R.drawable.menu_item_prenotazione));
        albumsItems.add(new MenuItem("Album 5", R.drawable.menu_item_live));
    }
}
