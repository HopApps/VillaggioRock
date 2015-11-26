package it.hopapps.villaggiorock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.Spotify;


import it.hopapps.villaggiorock.adapters.PlaylistAdapter;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PlaylistActivity extends AppCompatActivity implements PlayerNotificationCallback, ConnectionStateCallback {
    private Context ctx = this;
    private static final int REQUEST_CODE = 1342;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        AuthenticationRequest.Builder autenticationRequestBuilder = new AuthenticationRequest.Builder(ctx.getString(R.string.spotify_client_id), AuthenticationResponse.Type.TOKEN, ctx.getString(R.string.spotify_redirect_uri));
        autenticationRequestBuilder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = autenticationRequestBuilder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        final Button playButton = (Button) findViewById(R.id.play_button);
        playButton.setClickable(false);

        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                case TOKEN:
                    SpotifyApi spotifyApi = new SpotifyApi();
                    spotifyApi.setAccessToken(response.getAccessToken());
                    SpotifyService spotifyService = spotifyApi.getService();
                    Callback<Playlist> callbackSpotifyPlaylist = new Callback<Playlist>() {
                        @Override
                        public void success(final Playlist playlist, Response response) {
                            TextView playlistTitle = (TextView) findViewById(R.id.playlist_title);
                            playlistTitle.setText(playlist.name);

                            final Pager<PlaylistTrack> playlistTrackPager = playlist.tracks;

                            ListView playlistListView = (ListView) findViewById(R.id.list_view_playlist);
                            playlistListView.setAdapter(new PlaylistAdapter(ctx, playlistTrackPager));

                            playButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH);
                                    intent.setData(Uri.parse("spotify:user:"+ctx.getString(R.string.user_id)+":playlist:"+ctx.getString(R.string.playlist_id)));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                    if (intent.resolveActivity(getPackageManager()) != null) {
                                        startActivity(intent);
                                    }
                                    else {
                                        Intent spotifyPlayStoreIntent = new Intent(Intent.ACTION_VIEW);
                                        spotifyPlayStoreIntent.setData(Uri.parse(ctx.getString(R.string.spotify_playstore_uri)));
                                        startActivity(spotifyPlayStoreIntent);
                                    }
                                }
                            });
                            playButton.setClickable(true);
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
                    };
                    spotifyService.getPlaylist(ctx.getString(R.string.user_id), ctx.getString(R.string.playlist_id), callbackSpotifyPlaylist);
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
