package it.hopapps.villaggiorock.asyncTasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.camera2.params.Face;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import it.hopapps.villaggiorock.EventsActivity;
import it.hopapps.villaggiorock.R;
import it.hopapps.villaggiorock.adapters.EventsAdapter;
import it.hopapps.villaggiorock.adapters.RVMenuAdapter;
import it.hopapps.villaggiorock.models.EventsItem;
import it.hopapps.villaggiorock.models.MenuItem;

/**
 * Created by sebastian on 10/11/15.
 */
public class FacebookEventsRetriever extends AsyncTask<Void, Void, Void>{

    JSONArray jsonArray;
    JSONObject jsonObject;
    JSONObject jsonObjectImage;
    Context context;
    RecyclerView rv;

    public FacebookEventsRetriever(Context c, RecyclerView rv){
        this.context = c;
        this.rv = rv;
    }

    @Override
    protected Void doInBackground(Void... params) {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/1638904126342989/events",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        jsonObject = response.getJSONObject();
                    }
                }
        ).executeAndWait();
        try {
            jsonArray = jsonObject.getJSONArray("data");

            Bundle parameters = new Bundle();
            parameters.putString("fields", "cover");

            GraphRequest gr = new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    jsonArray.getJSONObject(0).getString("id"),
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                            jsonObjectImage = response.getJSONObject();
                        }
                    }
            );
            gr.setParameters(parameters);
            gr.executeAndWait();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        ArrayList eventsItems = new ArrayList<>();
        try {
            eventsItems.add(new EventsItem(jsonArray.getJSONObject(0).getString("name"), jsonObjectImage.getJSONObject("cover").getString("source")));
            eventsItems.add(new EventsItem(jsonArray.getJSONObject(1).getString("name"), jsonObjectImage.getJSONObject("cover").getString("source")));
            EventsAdapter adapter = new EventsAdapter(eventsItems, context);
            rv.setAdapter(adapter);
            rv.setHasFixedSize(true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}