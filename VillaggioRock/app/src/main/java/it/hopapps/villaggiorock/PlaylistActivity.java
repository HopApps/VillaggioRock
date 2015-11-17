package it.hopapps.villaggiorock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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

    // Old onCreate()
    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }*/

    // TODO: Replace with your client ID
    private static final String CLIENT_ID = "704f3714f0834c76afd4e549b92760e0";
    // TODO: Replace with your redirect URI
    private static final String REDIRECT_URI = "villaggiorock-app-login.it://callback";

    private Player mPlayer;

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

        /*playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager packageManager = getPackageManager();
                if (packageManager != null) {
                    Intent spotifyIntent = packageManager.getLaunchIntentForPackage(ctx.getString(R.string.spotify_package_name));
                    startActivity(spotifyIntent);
                }
                else {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + ctx.getString(R.string.spotify_package_name))));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + ctx.getString(R.string.spotify_package_name))));
                    }
                }
            }
        });*/

        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                case TOKEN:
                    SpotifyApi spotifyApi = new SpotifyApi();
                    spotifyApi.setAccessToken(response.getAccessToken());
                    SpotifyService spotifyService = spotifyApi.getService();
                    spotifyService.getPlaylist(ctx.getString(R.string.user_id), ctx.getString(R.string.playlist_id), new Callback<Playlist>() {
                        @Override
                        public void success(Playlist playlist, Response response) {
                            Log.d("playlist success", playlist.name);
                            TextView playlistTitle = (TextView) findViewById(R.id.playlist_title);
                            playlistTitle.setText(playlist.name);
                            playButton.setClickable(true);
                            bigPlayButton.setClickable(true);

                            Pager<PlaylistTrack> playlistTrackPager = playlist.tracks;

                            ListView playlistListView = (ListView) findViewById(R.id.list_view_playlist);
                            playlistListView.setAdapter(new PlaylistAdapter(ctx, playlistTrackPager));

                            for (int i = 0; i < playlistTrackPager.items.size(); i++) {
                                Log.d("Track n° " + Integer.toString(i), String.valueOf(playlistTrackPager.items.get(i).track.name));
                                Log.d("Track n° " + Integer.toString(i), String.valueOf(playlistTrackPager.items.get(i).track.album.name));
                                for (int j = 0; j < playlistTrackPager.items.get(i).track.artists.size(); j++) {
                                    Log.d("Track n° " + Integer.toString(i), String.valueOf(playlistTrackPager.items.get(i).track.artists.get(j).name));
                                }
                                Log.d("Track n° " + Integer.toString(i), formatDuration(playlistTrackPager.items.get(i).track.duration_ms));
                            }
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

    protected String formatDuration (long duration) {
        long sDuration = (duration / 1000) % 60 ;
        long mDuration = ((duration / (1000*60)) % 60);
        long hDuration = ((duration / (1000*60*60)) % 24);
        if (hDuration != 0) {
            return Long.toString(hDuration) + ":" + Long.toString(mDuration) + ":" + Long.toString(sDuration);
        }
        else {
            return Long.toString(mDuration) + ":" + Long.toString(sDuration);
        }
    }

}
