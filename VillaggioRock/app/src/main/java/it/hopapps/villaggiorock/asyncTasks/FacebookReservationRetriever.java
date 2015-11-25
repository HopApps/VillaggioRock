package it.hopapps.villaggiorock.asyncTasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;
import org.json.JSONObject;

import it.hopapps.villaggiorock.R;

public class FacebookReservationRetriever  extends AsyncTask<Void, Void, Void> {
    private Context context;
    private String eventId;
    private JSONObject jsonObjectEvent;
    private JSONObject jsonObjectMe;
    private JSONObject jsonObjectCover;
    private JSONObject jsonObjectEmail;

    public FacebookReservationRetriever(String eventId, Context c){
        this.context = c;
        this.eventId = eventId;
    }

    @Override
    protected Void doInBackground(Void... params) {
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/"+eventId, null, HttpMethod.GET, new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        jsonObjectEvent = response.getJSONObject();
                    }
                })
                .executeAndWait();
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me", null, HttpMethod.GET, new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        jsonObjectMe = response.getJSONObject();
                    }
                })
                .executeAndWait();

        Bundle parameters = new Bundle();
        parameters.putString("fields", "cover");

        GraphRequest coverRequest = new GraphRequest(AccessToken.getCurrentAccessToken(), "/"+eventId, null, HttpMethod.GET, new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        jsonObjectCover = response.getJSONObject();
                    }
                }
        );

        coverRequest.setParameters(parameters);
        coverRequest.executeAndWait();

        parameters.putString("fields", "email");
        GraphRequest emailRequest = new GraphRequest(AccessToken.getCurrentAccessToken(), "/me", null, HttpMethod.GET, new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        jsonObjectEmail = response.getJSONObject();
                    }
                }
        );
        emailRequest.setParameters(parameters);
        emailRequest.executeAndWait();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) ((Activity)context).findViewById(R.id.toolbar_layout);
        AppBarLayout appBarLayout = (AppBarLayout) ((Activity)context).findViewById(R.id.app_bar);
        EditText mailEditText = (EditText) ((Activity)context).findViewById(R.id.et_mail);
        TextView hiddenName = (TextView) ((Activity)context).findViewById(R.id.tv_name_hidden);
        try {
            collapsingToolbar.setTitle(jsonObjectEvent.getString("name"));
            collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

            hiddenName.setText(jsonObjectMe.getString("name"));
            mailEditText.setText(jsonObjectEmail.getString("email"));

            new AppBarLayoutBackgroundSetter(context, appBarLayout, jsonObjectCover.getJSONObject("cover").getString("source")).execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
