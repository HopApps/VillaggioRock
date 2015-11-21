package it.hopapps.villaggiorock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;
import it.hopapps.villaggiorock.adapters.RVMenuAdapter;
import it.hopapps.villaggiorock.models.MenuItem;

public class MenuActivity extends AppCompatActivity {

    private List<MenuItem> menuItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
        setContentView(R.layout.activity_menu);
        initializeMenuItems();
        RecyclerView rv = (RecyclerView) findViewById(R.id.recicler_view_activity_menu);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        RVMenuAdapter adapter = new RVMenuAdapter(menuItems, MenuActivity.this);
        rv.setAdapter(adapter);
        rv.setHasFixedSize(true);
    }

    private void initializeMenuItems(){
        menuItems = new ArrayList<>();
        menuItems.add(new MenuItem(getResources().getString(R.string.events_menu_name), R.drawable.menu_item_events));
        menuItems.add(new MenuItem(getResources().getString(R.string.photos_menu_name), R.drawable.menu_item_foto));
        menuItems.add(new MenuItem(getResources().getString(R.string.playlist_menu_name), R.drawable.menu_item_playlist));
        //menuItems.add(new MenuItem(getResources().getString(R.string.reservation_menu_name), R.drawable.menu_item_prenotazione));
        menuItems.add(new MenuItem(getResources().getString(R.string.gadget_menu_name), R.drawable.menu_item_gadget));
        menuItems.add(new MenuItem(getResources().getString(R.string.live_menu_name), R.drawable.menu_item_live));
    }
}
