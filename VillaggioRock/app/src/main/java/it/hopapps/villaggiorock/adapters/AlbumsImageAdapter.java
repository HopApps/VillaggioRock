package it.hopapps.villaggiorock.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.List;
import it.hopapps.villaggiorock.R;
import it.hopapps.villaggiorock.models.AlbumsItem;

public class AlbumsImageAdapter extends BaseAdapter {
    private Context context;
    private List<AlbumsItem> albumsItems;
    private DisplayImageOptions options;
    private LayoutInflater inflater;

    public AlbumsImageAdapter(Context context, List < AlbumsItem > albumsItems) {
        this.context = context;
        this.albumsItems = albumsItems;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.albums_layout, parent, false);
            holder = new ViewHolder();
            assert view != null;
            holder.imageView = (ImageView) view.findViewById(R.id.grid_albums_image);
            holder.textView = (TextView) view.findViewById(R.id.grid_albums_label);
            holder.textView.setText(albumsItems.get(position).getName());
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        ImageLoader.getInstance().displayImage(albumsItems.get(position).getPhotoUrl(), holder.imageView, options);
        return view;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView textView;
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