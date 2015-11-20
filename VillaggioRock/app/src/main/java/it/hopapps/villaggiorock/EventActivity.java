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
import java.util.Arrays;

import it.hopapps.villaggiorock.asyncTasks.FacebookEventAttending;
import it.hopapps.villaggiorock.asyncTasks.FacebookEventRetriever;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Intent i = this.getIntent();
        final String eventId = i.getExtras().getString("id");
        FacebookEventRetriever fber = new FacebookEventRetriever(eventId, this);
        fber.execute();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button reservationButton = (Button) findViewById(R.id.reserve_button);
        reservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventActivity.this, ReservationActivity.class);
                startActivity(intent);
            }
        });
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!AccessToken.getCurrentAccessToken().getPermissions().contains("rsvp_event")) {
                    LoginManager.getInstance().logInWithPublishPermissions(EventActivity.this, Arrays.asList("rsvp_event"));
                    FacebookEventAttending fea = new FacebookEventAttending(eventId, fab, view);
                    fea.execute();
                } else {
                    FacebookEventAttending fea = new FacebookEventAttending(eventId, fab, view);
                    fea.execute();
                }

            }
        });
    }

}
