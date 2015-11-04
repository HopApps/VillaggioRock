package it.hopapps.villaggiorock.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import it.hopapps.villaggiorock.R;
import it.hopapps.villaggiorock.models.MenuItem;

public class AlbumsImageAdapter extends BaseAdapter {
    private Context context;
    private List<MenuItem> albumsItems;

    public AlbumsImageAdapter(Context context, List<MenuItem> albumsItems) {
        this.context = context;
        this.albumsItems = albumsItems;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;
        if (convertView == null) {
            //gridView = new View(context);
            gridView = inflater.inflate(R.layout.albums_layout, null);
            TextView textView = (TextView) gridView.findViewById(R.id.grid_albums_label);
            textView.setText(albumsItems.get(position).getName());
            ImageView imageView = (ImageView) gridView.findViewById(R.id.grid_albums_image);
            imageView.setImageResource(albumsItems.get(position).getPhotoId());
        } else {
            gridView = (View) convertView;
        }
        return gridView;
    }

    @Override
    public int getCount() {
        return albumsItems.size();
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