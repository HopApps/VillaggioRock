package it.hopapps.villaggiorock.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import it.hopapps.villaggiorock.R;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistTrack;

/**
 * Created by francesco on 16/11/15.
 */
public class PlaylistAdapter extends BaseAdapter {

    private Context mContext;
    private Pager<PlaylistTrack> playlistTrackPager;

    public PlaylistAdapter(Context c, Pager<PlaylistTrack> playlistTrackPager) {
        mContext = c;
        this.playlistTrackPager = playlistTrackPager;
    }

    @Override
    public int getCount() {
        return playlistTrackPager.items.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View playlistView;
        if (convertView == null) {
            playlistView = inflater.inflate(R.layout.playlist_table_row_layout, null);

            TextView nameTv = (TextView) playlistView.findViewById(R.id.track_name);
            nameTv.setText(playlistTrackPager.items.get(position).track.name);

            TextView albumTv = (TextView) playlistView.findViewById(R.id.track_album);
            albumTv.setText(playlistTrackPager.items.get(position).track.album.name);

            TextView artistTv = (TextView) playlistView.findViewById(R.id.track_artist);
            String partial = "";
            for (int i = 0; i < playlistTrackPager.items.get(position).track.artists.size(); i++) {
                partial = partial + playlistTrackPager.items.get(position).track.artists.get(i).name;
            }
            artistTv.setText(partial);

            TextView durationTv = (TextView) playlistView.findViewById(R.id.track_duration);
            String duration = formatDuration(playlistTrackPager.items.get(position).track.duration_ms);
            durationTv.setText(duration);
        }
        else {
            playlistView = convertView;
        }
        return playlistView;

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
