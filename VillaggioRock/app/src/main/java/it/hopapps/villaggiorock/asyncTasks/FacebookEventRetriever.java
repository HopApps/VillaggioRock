package it.hopapps.villaggiorock.asyncTasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import it.hopapps.villaggiorock.R;

public class FacebookEventRetriever extends AsyncTask<Void, Void, Void> {

    JSONObject jsonObjectEvent;
    JSONObject jsonObjectCover;
    JSONObject jsonObjectAttendingNumber;
    String eventId;
    Context context;

    public FacebookEventRetriever(String eventId, Context c){
        this.context = c;
        this.eventId = eventId;
    }

    @Override
    protected Void doInBackground(Void... params) {
        new GraphRequest(
            AccessToken.getCurrentAccessToken(),
            "/"+eventId,
            null,
            HttpMethod.GET,
            new GraphRequest.Callback() {
                public void onCompleted(GraphResponse response) {
                    jsonObjectEvent = response.getJSONObject();
                }
            }
        ).executeAndWait();
        Bundle parameters = new Bundle();
        parameters.putString("fields", "cover");
        GraphRequest coverRequest = new GraphRequest(
            AccessToken.getCurrentAccessToken(),
            "/"+eventId,
            null,
            HttpMethod.GET,
            new GraphRequest.Callback() {
                public void onCompleted(GraphResponse response) {
                    jsonObjectCover = response.getJSONObject();
                }
            }
        );
        coverRequest.setParameters(parameters);
        coverRequest.executeAndWait();
        parameters = new Bundle();
        parameters.putString("fields", "attending_count");
        GraphRequest attendingNumberRequest = new GraphRequest(
            AccessToken.getCurrentAccessToken(),
            "/"+eventId,
            null,
            HttpMethod.GET,
            new GraphRequest.Callback() {
                public void onCompleted(GraphResponse response) {
                    jsonObjectAttendingNumber = response.getJSONObject();
                }
            }
        );
        attendingNumberRequest.setParameters(parameters);
        attendingNumberRequest.executeAndWait();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) ((Activity)context).findViewById(R.id.collapsing_toolbar);
        AppBarLayout appBarLayout = (AppBarLayout) ((Activity)context).findViewById(R.id.app_bar);
        TextView dateTV = (TextView) ((Activity)context).findViewById(R.id.tv_date);
        TextView timeTV = (TextView) ((Activity)context).findViewById(R.id.tv_time);
        TextView placeTV = (TextView) ((Activity)context).findViewById(R.id.tv_place);
        TextView goingTV = (TextView) ((Activity)context).findViewById(R.id.tv_going);
        TextView descriptionTV = (TextView) ((Activity)context).findViewById(R.id.description);
        try {
            collapsingToolbar.setTitle(jsonObjectEvent.getString("name"));
            collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
            AppBarLayoutBackgroundSetter ablbs = new AppBarLayoutBackgroundSetter(context, appBarLayout, jsonObjectCover.getJSONObject("cover").getString("source"));
            ablbs.execute();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date date = dateFormat.parse(jsonObjectEvent.getString("start_time"));
            dateTV.setText(Integer.toString(date.getDate())+"/"+Integer.toString(date.getMonth()+1)+"/"+Integer.toString(date.getYear()+1900));
            timeTV.setText(Integer.toString(date.getHours())+":"+Integer.toString(date.getMinutes()));
            JSONObject place = jsonObjectEvent.getJSONObject("place");
            JSONObject location = place.getJSONObject("location");
            placeTV.setText(place.getString("name") + ", " + location.getString("street")+ " " + location.getString("city"));
            goingTV.setText(jsonObjectAttendingNumber.getString("attending_count"));
            descriptionTV.setText(jsonObjectEvent.getString("description"));
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}