package it.hopapps.villaggiorock;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ShowImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        setContentView(R.layout.activity_show_image);

        String eventId = this.getIntent().getExtras().getString("id");

        ImageLoader.getInstance().displayImage(
                eventId,
                (ImageView) findViewById(R.id.fullscreen_photo_image_view),
                options
        );
    }
}
