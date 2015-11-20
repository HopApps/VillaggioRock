package it.hopapps.villaggiorock;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ShowImageActivity extends AppCompatActivity {

    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        setContentView(R.layout.activity_show_image);

        Intent i = this.getIntent();
        String eventId = i.getExtras().getString("id");

        ImageView img = (ImageView) findViewById(R.id.fullscreen_photo_image_view);
        ImageLoader.getInstance().displayImage(eventId, img, options);
    }
}
