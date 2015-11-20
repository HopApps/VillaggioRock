package it.hopapps.villaggiorock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class LiveActivity extends AppCompatActivity {

    Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.twitter_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tweetIntent = new Intent(Intent.ACTION_SEND);
                EditText tweetEt = (EditText) findViewById(R.id.tweet_text);
                String tweet = tweetEt.getText().toString();

                tweetIntent.putExtra(Intent.EXTRA_TEXT, tweet + ctx.getString(R.string.hashtag));
                tweetIntent.setType("text/plain");

                PackageManager packManager = getPackageManager();
                List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

                boolean resolved = false;
                for(ResolveInfo resolveInfo: resolvedInfoList){
                    if(resolveInfo.activityInfo.packageName.startsWith(ctx.getString(R.string.twitter_app_name))){
                        tweetIntent.setClassName(
                                resolveInfo.activityInfo.packageName,
                                resolveInfo.activityInfo.name );
                        resolved = true;
                        break;
                    }
                }
                if(resolved){
                    startActivity(tweetIntent);
                }
                else {
                    String uri = ctx.getString(R.string.twitter_playstore_uri);
                    Intent webIntent = new Intent(Intent.ACTION_VIEW);
                    webIntent.setData(Uri.parse(uri));
                    startActivity(webIntent);
                }

            }
        });
    }

}
