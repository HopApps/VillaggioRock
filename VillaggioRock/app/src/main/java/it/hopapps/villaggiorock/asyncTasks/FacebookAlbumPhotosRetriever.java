package it.hopapps.villaggiorock.asyncTasks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import it.hopapps.villaggiorock.ShowImageActivity;
import it.hopapps.villaggiorock.adapters.ImageGridAdapter;

public class FacebookAlbumPhotosRetriever extends AsyncTask<Void, Void, Void> {

    JSONObject jsonObjectPhotos;
    JSONArray jsonPhotosArray;
    List<String> urlPhotos;
    String albumId;
    GridView gridview;
    Context context;
    

    public FacebookAlbumPhotosRetriever(String albumId, Context c, GridView gridview){
        this.context = c;
        this.albumId = albumId;
        urlPhotos = new ArrayList<>();
        this.gridview = gridview;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Bundle parameters = new Bundle();
        parameters.putString("fields", "photos{images}");
        GraphRequest coverRequest = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+albumId,
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        jsonObjectPhotos = response.getJSONObject();
                        try {
                            jsonPhotosArray = jsonObjectPhotos.getJSONObject("photos").getJSONArray("data");
                            for (int i = 0; i<jsonPhotosArray.length();i++){
                                urlPhotos.add(jsonPhotosArray.getJSONObject(i).getJSONArray("images").getJSONObject(0).getString("source"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        GraphRequest nextRequest = response.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);

                        if(nextRequest != null && !nextRequest.getGraphPath().equals(response.getRequest().getGraphPath())){
                            nextRequest.setCallback(this);
                            nextRequest.executeAndWait();
                        }
                    }
                }
        );
        coverRequest.setParameters(parameters);
        coverRequest.executeAndWait();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        gridview.setAdapter(new ImageGridAdapter(context, urlPhotos));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String eventId = urlPhotos.get(position);
                Intent intent = new Intent(context, ShowImageActivity.class);
                intent.putExtra("id", eventId);
                context.startActivity(intent);
            }
        });
    }
}