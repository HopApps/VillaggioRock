package it.hopapps.villaggiorock.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.List;

import it.hopapps.villaggiorock.R;

public class ImageGridAdapter extends BaseAdapter {
    private List<String> urlPhotos;
    private DisplayImageOptions options;
    private LayoutInflater inflater;

    public ImageGridAdapter(Context c, List<String> urlPhotos) {
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        this.urlPhotos = urlPhotos;
        inflater = LayoutInflater.from(c);
    }

    public int getCount() {
        return urlPhotos.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.album_photo, parent, false);
            holder = new ViewHolder();
            assert view != null;
            holder.imageView = (ImageView) view.findViewById(R.id.grid_item_image);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        ImageLoader.getInstance().displayImage(urlPhotos.get(position), holder.imageView, options);
        return view;
    }

    static class ViewHolder {
        ImageView imageView;
    }
}
