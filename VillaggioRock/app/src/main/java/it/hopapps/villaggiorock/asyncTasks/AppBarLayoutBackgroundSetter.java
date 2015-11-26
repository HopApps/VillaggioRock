package it.hopapps.villaggiorock.asyncTasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;

public class AppBarLayoutBackgroundSetter extends AsyncTask<Void, Void, Bitmap> {
    private final WeakReference<AppBarLayout> imageViewReference;
    private String data;
    private Context context;

    public AppBarLayoutBackgroundSetter(Context c, AppBarLayout imageView, String s) {
        imageViewReference = new WeakReference<>(imageView);
        data = s;
        context = c;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            URL url = new URL(data);
            return BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            final AppBarLayout imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setBackground(new BitmapDrawable(context.getResources(), bitmap));
            }
        }
    }
}