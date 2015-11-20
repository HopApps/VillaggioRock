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
        RecyclerView rv = (RecyclerView) findViewById(R.id.recicler_view_events_menu);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        FacebookEventsRetriever fbER = new FacebookEventsRetriever(this, rv);
        fbER.execute();
    }
}
