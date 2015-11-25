package it.hopapps.villaggiorock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import it.hopapps.villaggiorock.asyncTasks.FacebookEventsRetriever;

public class EventsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_events);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recicler_view_events_menu);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        new FacebookEventsRetriever(this, recyclerView).execute();
    }
}
