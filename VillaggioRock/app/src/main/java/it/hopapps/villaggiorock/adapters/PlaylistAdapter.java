package it.hopapps.villaggiorock.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import it.hopapps.villaggiorock.R;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistTrack;

public class PlaylistAdapter extends BaseAdapter {

    private Context mContext;
    private Pager<PlaylistTrack> playlistTrackPager;

    public PlaylistAdapter(Context c, Pager<PlaylistTrack> playlistTrackPager) {
        mContext = c;
        this.playlistTrackPager = playlistTrackPager;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View playlistView;
        if (convertView == null) {
            playlistView = inflater.inflate(R.layout.playlist_table_row_layout, null);

            TextView nameTv = (TextView) playlistView.findViewById(R.id.track_name);
            nameTv.setText(playlistTrackPager.items.get(position).track.name);

            TextView artistTv = (TextView) playlistView.findViewById(R.id.track_artist);
            String artistString = "";
            for (int i = 0; i < playlistTrackPager.items.get(position).track.artists.size(); i++) {
                artistString += playlistTrackPager.items.get(position).track.artists.get(i).name;
            }
            artistTv.setText(artistString);

            TextView durationTv = (TextView) playlistView.findViewById(R.id.track_duration);
            durationTv.setText(formatDuration(playlistTrackPager.items.get(position).track.duration_ms));
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
            return Long.toString(hDuration) + (mDuration < 10 ? ":0" + Long.toString(mDuration) : ":" + Long.toString(mDuration)) + (sDuration < 10 ? ":0" + Long.toString(sDuration) : ":" + Long.toString(sDuration));
        }
        else {
            return Long.toString(mDuration) + (sDuration < 10 ? ":0" + Long.toString(sDuration) : ":" + Long.toString(sDuration));
        }
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
}
