package it.hopapps.villaggiorock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.hopapps.villaggiorock.models.MenuItem;

public class MenuActivity extends AppCompatActivity {

    private List<MenuItem> menuItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecyclerView rv = (RecyclerView)findViewById(R.id.recicler_view_activity_menu);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        initializeMenuItems();
        setMenu(rv, llm);

    }

    private void setMenu (RecyclerView rv, LinearLayoutManager llm){
        setContentView(R.layout.activity_menu);
        rv.setLayoutManager(llm);

    }

    private void initializeMenuItems(){
        menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("Events", R.drawable.menuItemEvents));
        menuItems.add(new MenuItem("Foto", R.drawable.menuItemFoto));
        menuItems.add(new MenuItem("Playlist", R.drawable.menuItemPlaylist));
        menuItems.add(new MenuItem("Prenotazione", R.drawable.menuItemPrenotazione));
        menuItems.add(new MenuItem("Gadget", R.drawable.menuItemGadget));
        menuItems.add(new MenuItem("Live", R.drawable.menuItemLive));
    }
}
