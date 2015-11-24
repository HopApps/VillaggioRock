package it.hopapps.villaggiorock;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

public class VillaggioRockApplication extends Application {

    @Override
    public void onCreate (){
        super.onCreate();
        Parse.initialize(this, "Wg9KBw13bDVNrqoyasDikEcSefBJbScPnitl2vBx", "cA6wAO7GYhwc6twfa9Qwn6vbsweEuPF4JqBgo2W3");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

}
