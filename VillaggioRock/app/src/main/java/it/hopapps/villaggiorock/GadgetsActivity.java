package it.hopapps.villaggiorock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.hopapps.villaggiorock.adapters.RVMenuAdapter;
import it.hopapps.villaggiorock.models.CustomMenuItem;

public class GadgetsActivity extends AppCompatActivity {

    private List<CustomMenuItem> gadgetsItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gadgets);
        initializeMenuItems();
        RecyclerView rv = (RecyclerView) findViewById(R.id.recicler_view_gadgets_menu);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        RVMenuAdapter adapter = new RVMenuAdapter(gadgetsItems, GadgetsActivity.this);
        rv.setAdapter(adapter);
        rv.setHasFixedSize(true);
    }

    private void initializeMenuItems(){
        gadgetsItems = new ArrayList<>();
        gadgetsItems.add(new CustomMenuItem("Gadget 1", R.drawable.menu_item_events));
        gadgetsItems.add(new CustomMenuItem("Gadget 2", R.drawable.menu_item_foto));
    }
}
