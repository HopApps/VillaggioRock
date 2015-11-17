package it.hopapps.villaggiorock;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.Spotify;

import java.util.List;

import it.hopapps.villaggiorock.adapters.PlaylistAdapter;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Track;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PlaylistActivity extends AppCompatActivity implements
        PlayerNotificationCallback, ConnectionStateCallback {

    Context ctx = this;

    // TODO: Replace with your client ID
    private static final String CLIENT_ID = "704f3714f0834c76afd4e549b92760e0";
    // TODO: Replace with your redirect URI
    private static final String REDIRECT_URI = "villaggiorock-app-login.it://callback";

    private static final int REQUEST_CODE = 1342;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        final Button playButton = (Button) findViewById(R.id.play_button);
        playButton.setClickable(false);
        final FloatingActionButton bigPlayButton = (FloatingActionButton) findViewById(R.id.play_fab);
        bigPlayButton.setClickable(false);

        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                case TOKEN:
                    SpotifyApi spotifyApi = new SpotifyApi();
                    spotifyApi.setAccessToken(response.getAccessToken());
                    SpotifyService spotifyService = spotifyApi.getService();
                    spotifyService.getPlaylist(ctx.getString(R.string.user_id), ctx.getString(R.string.playlist_id), new Callback<Playlist>() {
                        @Override
                        public void success(final Playlist playlist, Response response) {
                            Log.d("playlist success", playlist.name);
                            TextView playlistTitle = (TextView) findViewById(R.id.playlist_title);
                            playlistTitle.setText(playlist.name);
                            playButton.setClickable(true);
                            bigPlayButton.setClickable(true);

                            final Pager<PlaylistTrack> playlistTrackPager = playlist.tracks;

                            ListView playlistListView = (ListView) findViewById(R.id.list_view_playlist);
                            playlistListView.setAdapter(new PlaylistAdapter(ctx, playlistTrackPager));

                            playButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent spotifyIntent = new Intent();
                                    spotifyIntent.setClassName(ctx.getString(R.string.spotify_package_name), ctx.getString(R.string.spotify_launcher));

                                    PackageManager packageManager = getPackageManager();
                                    List<ResolveInfo> activities = packageManager.queryIntentActivities(spotifyIntent, 0);
                                    boolean isIntentSafe = activities.size() > 0;

                                    if (isIntentSafe) {
                                        spotifyIntent.putExtra(SearchManager.QUERY, playlist.name);
                                        startActivity(spotifyIntent);
                                    }
                                    else {
                                        Uri playStoreSpotify = Uri.parse(ctx.getString(R.string.spotify_playstore_uri));
                                        Intent playStoreIntent = new Intent(Intent.ACTION_VIEW, playStoreSpotify);
                                        startActivity(playStoreIntent);
                                    }
                                }
                            });
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.d("playlist error", error.getMessage());
                            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                            builder
                                    .setTitle(ctx.getString(R.string.playlist_loading_failure_dialog_title))
                                    .setMessage(ctx.getString(R.string.playlist_loading_failure_dialog_body))
                                    .setCancelable(false)
                                    .setIcon(R.drawable.error)
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(ctx, MenuActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    });
                    break;
                case ERROR:
                    break;
                default:
            }
        }
    }

    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");
    }

    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Throwable error) {
        Log.d("MainActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }

    @Override
    public void onPlaybackEvent(EventType eventType, PlayerState playerState) {
        Log.d("MainActivity", "Playback event received: " + eventType.name());
    }

    @Override
    public void onPlaybackError(ErrorType errorType, String errorDetails) {
        Log.d("MainActivity", "Playback error received: " + errorType.name());
    }

    @Override
    protected void onDestroy() {
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

}
