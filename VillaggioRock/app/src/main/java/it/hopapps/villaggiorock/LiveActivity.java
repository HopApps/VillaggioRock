package it.hopapps.villaggiorock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.List;

public class LiveActivity extends AppCompatActivity {
    private Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.twitter_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText tweetEt = (EditText) findViewById(R.id.tweet_text);
                String tweet = tweetEt.getText().toString();
                Intent tweetIntent = new Intent(Intent.ACTION_SEND);
                tweetIntent.putExtra(Intent.EXTRA_TEXT, tweet + " " + ctx.getString(R.string.hashtag));
                tweetIntent.setType("text/plain");

                List<ResolveInfo> resolvedInfoList = getPackageManager().queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);
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
                } else {
                    Intent webIntent = new Intent(Intent.ACTION_VIEW);
                    webIntent.setData(Uri.parse(ctx.getString(R.string.twitter_playstore_uri)));
                    startActivity(webIntent);
                }

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });
    }

}
