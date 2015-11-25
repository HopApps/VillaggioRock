package it.hopapps.villaggiorock;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import java.util.Collections;
import java.util.List;

public class SplashScreen extends AppCompatActivity {
    private CallbackManager callbackManager;
    private List permissions = Collections.singletonList("email");

    FacebookCallback fbCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
        }

        @Override
        public void onCancel() {
            Snackbar.make(findViewById(android.R.id.content), getString(R.string.splashscreen_login_canceled), Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }

        @Override
        public void onError(FacebookException exception) {
            Snackbar.make(findViewById(android.R.id.content), getString(R.string.splashscreen_login_failed), Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_splash_screen);

        LoginButton connectWithFacebookButton = (LoginButton) findViewById(R.id.buttonLogin);
        connectWithFacebookButton.setReadPermissions(permissions);
        connectWithFacebookButton.registerCallback(callbackManager, fbCallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
