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

import it.hopapps.villaggiorock.AlbumPhotosActivity;
import it.hopapps.villaggiorock.R;
import it.hopapps.villaggiorock.adapters.AlbumsImageAdapter;
import it.hopapps.villaggiorock.models.AlbumsItem;

public class FacebookAlbumsRetriever extends AsyncTask<Void, Void, Void> {

    private JSONObject jsonObjectAlbums;
    private JSONObject jsonObjectAlbumsCovers;
    private JSONArray jsonAlbumsArray;
    private JSONArray jsonAlbumsCovers;
    private Context context;
    private GridView gridview;

    public FacebookAlbumsRetriever(Context c, GridView gridview) {
        this.context = c;
        this.gridview = gridview;
    }


    @Override
    protected Void doInBackground(Void... params) {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+context.getString(R.string.villaggiorock_page)+"/albums",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        jsonObjectAlbums = response.getJSONObject();
                    }
                }
        ).executeAndWait();

        Bundle parameters = new Bundle();
        parameters.putString("fields", "picture");

        GraphRequest albumCovers = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/"+context.getString(R.string.villaggiorock_page)+"/albums",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        jsonObjectAlbumsCovers = response.getJSONObject();
                    }
                }
        );

        albumCovers.setParameters(parameters);
        albumCovers.executeAndWait();

        try {
            jsonAlbumsArray = jsonObjectAlbums.getJSONArray("data");
            jsonAlbumsCovers = jsonObjectAlbumsCovers.getJSONArray("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        final List<AlbumsItem> albumsItems = new ArrayList<>();
        try {
            for (int i = 0; i < jsonAlbumsArray.length(); i++) {
                albumsItems.add(new AlbumsItem(
                        jsonAlbumsArray.getJSONObject(i).getString("id"),
                        jsonAlbumsArray.getJSONObject(i).getString("name"),
                        jsonAlbumsCovers.getJSONObject(i).getJSONObject("picture").getJSONObject("data").getString("url"))
                );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        gridview.setAdapter(new AlbumsImageAdapter(context, albumsItems));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(context, AlbumPhotosActivity.class);
                intent.putExtra("id", albumsItems.get(position).getId());
                context.startActivity(intent);
            }
        });
    }
}
