package it.hopapps.villaggiorock;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import java.util.Collections;

import it.hopapps.villaggiorock.asyncTasks.FacebookEventAttending;
import it.hopapps.villaggiorock.asyncTasks.FacebookEventRetriever;

public class EventActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        eventId = this.getIntent().getExtras().getString("id");

        new FacebookEventRetriever (eventId, this).execute();

        setContentView(R.layout.activity_event);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button reservationButton = (Button) findViewById(R.id.reserve_button);
        reservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventActivity.this, ReservationActivity.class);
                intent.putExtra("id", eventId);
                startActivity(intent);
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setEnabled(false);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!AccessToken.getCurrentAccessToken().getPermissions().contains("rsvp_event")) {
                    LoginManager.getInstance().logInWithPublishPermissions(EventActivity.this, Collections.singletonList("rsvp_event"));
                } else {
                    new FacebookEventAttending(eventId, fab, view).execute();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        new FacebookEventAttending(eventId, fab, this.findViewById(R.id.event_layout_coordinator));
    }

}
