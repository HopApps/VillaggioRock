package it.hopapps.villaggiorock.asyncTasks;

import android.content.Context;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import it.hopapps.villaggiorock.R;
import it.hopapps.villaggiorock.adapters.EventsAdapter;
import it.hopapps.villaggiorock.models.EventsItem;

public class FacebookEventsRetriever extends AsyncTask<Void, Void, Void>{

    private JSONArray jsonDataArray;
    private JSONArray jsonCoversArray;
    private JSONObject jsonObjectEvents;
    private JSONObject jsonObjectCovers;
    private Context context;
    private RecyclerView rv;

    public FacebookEventsRetriever(Context c, RecyclerView rv){
        this.context = c;
        this.rv = rv;
    }

    @Override
    protected Void doInBackground(Void... params) {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+context.getString(R.string.villaggiorock_page)+"/events",
                null,
                HttpMethod.GET, new GraphRequest.Callback() {
                public void onCompleted(GraphResponse response) {
                    jsonObjectEvents = response.getJSONObject();
                }
            }).executeAndWait();

        Bundle parameters = new Bundle();
        parameters.putString("fields", "cover");

        GraphRequest coverRequest = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+context.getString(R.string.villaggiorock_page)+"/events",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                public void onCompleted(GraphResponse response) {
                    jsonObjectCovers = response.getJSONObject();
                }
            }
        );

        coverRequest.setParameters(parameters);
        coverRequest.executeAndWait();

        try {
            jsonDataArray = jsonObjectEvents.getJSONArray("data");
            jsonCoversArray = jsonObjectCovers.getJSONArray("data");
            filterOnlyFutureEvents();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void filterOnlyFutureEvents(){
        JSONArray jsonDataResult = new JSONArray();
        JSONArray jsonCoversResult = new JSONArray();
        for(int i = 0; i<jsonDataArray.length(); i++) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                Date date = dateFormat.parse(jsonDataArray.getJSONObject(i).getString("start_time"));
                if(date.after(Calendar.getInstance().getTime())){
                    jsonDataResult.put(jsonDataArray.getJSONObject(i));
                    jsonCoversResult.put(jsonCoversArray.getJSONObject(i));
                }
            } catch (JSONException | ParseException e) {
                e.printStackTrace();
            }
        }
        jsonDataArray = jsonDataResult;
        jsonCoversArray = jsonCoversResult;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        ArrayList eventsItems = new ArrayList<>();
        try {
            for(int i = 0; i<jsonDataArray.length(); i++) {
                eventsItems.add(new EventsItem(
                        jsonDataArray.getJSONObject(i).getString("id"),
                        jsonDataArray.getJSONObject(i).getString("name"),
                        jsonCoversArray.getJSONObject(i).getJSONObject("cover").getString("source"))
                );
            }
            EventsAdapter adapter = new EventsAdapter(eventsItems, context);
            rv.setAdapter(adapter);
            rv.setHasFixedSize(true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}