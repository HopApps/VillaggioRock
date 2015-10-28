package it.hopapps.villaggiorock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.hopapps.villaggiorock.adapters.RVMenuAdapter;
import it.hopapps.villaggiorock.models.MenuItem;

public class EventsActivity extends AppCompatActivity {

    private List<MenuItem> eventsItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        initializeMenuItems();
        RecyclerView rv = (RecyclerView) findViewById(R.id.recicler_view_events_menu);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        RVMenuAdapter adapter = new RVMenuAdapter(eventsItems);
        rv.setAdapter(adapter);
        rv.setHasFixedSize(true);
    }

    private void initializeMenuItems(){
        eventsItems = new ArrayList<>();
        eventsItems.add(new MenuItem("Evento 1", R.drawable.menu_item_events));
        eventsItems.add(new MenuItem("Evento 1", R.drawable.menu_item_foto));
    }
}
