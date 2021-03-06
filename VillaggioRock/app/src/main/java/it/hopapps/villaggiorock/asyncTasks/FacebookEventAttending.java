package it.hopapps.villaggiorock.asyncTasks;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import org.json.JSONException;
import org.json.JSONObject;

import it.hopapps.villaggiorock.R;

public class FacebookEventAttending extends AsyncTask<Void, Void, Void> {
    private Context context;
    private String eventID;
    private JSONObject JSONresponse;
    private FloatingActionButton fab;
    private View v;

    public FacebookEventAttending(String eventId, FloatingActionButton fab, View view){
        context = view.getContext();
        eventID = eventId;
        this.fab = fab;
        v = view;
    }

    @Override
    protected Void doInBackground(Void... params) {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + eventID + "/attending",
                null,
                HttpMethod.POST,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        JSONresponse = response.getJSONObject();
                    }
                }
        ).executeAndWait();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        try {
            if(JSONresponse.getString("success") != null){
                fab.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorPrimaryDark)));
                fab.setEnabled(false);
                fab.setImageDrawable(context.getResources().getDrawable(R.drawable.gone));
                Snackbar.make(v, R.string.going_snackbar, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
