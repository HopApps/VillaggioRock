package it.hopapps.villaggiorock.asyncTasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;
import org.json.JSONObject;

import it.hopapps.villaggiorock.R;

public class FacebookGadgetRetriever  extends AsyncTask<Void, Void, Void> {
    private Context context;
    private JSONObject jsonObjectMe;
    private JSONObject jsonObjectEmail;

    public FacebookGadgetRetriever(Context c){
        this.context = c;
    }

    @Override
    protected Void doInBackground(Void... params) {
        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me", null, HttpMethod.GET, new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        jsonObjectMe = response.getJSONObject();
                    }
                })
                .executeAndWait();

        Bundle parameters = new Bundle();
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
        EditText mailEditText = (EditText) ((Activity)context).findViewById(R.id.gadget_et_mail);
        TextView hiddenName = (TextView) ((Activity)context).findViewById(R.id.gadget_tv_name_hidden);
        try {
            hiddenName.setText(jsonObjectMe.getString("name"));
            mailEditText.setText(jsonObjectEmail.getString("email"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
