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
import java.util.Arrays;
import java.util.List;

public class SplashScreen extends AppCompatActivity {
    CallbackManager callbackManager;
    LoginButton connectWithFacebookButton;
    List permissions = Arrays.asList("email");

    FacebookCallback fbCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            //Intent intent = new Intent(SplashScreen.this, MenuActivity.class);
            //intent.putExtra(EXTRA_MESSAGE, message);
            //startActivity(intent);
            //Snackbar.make(findViewById(android.R.id.content), loginResult.getAccessToken().getUserId(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }

        @Override
        public void onCancel() {
            Snackbar.make(findViewById(android.R.id.content), "Login attempt canceled.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }

        @Override
        public void onError(FacebookException exception) {
            Snackbar.make(findViewById(android.R.id.content), "Login attempt failed.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_splash_screen);
        connectWithFacebookButton = (LoginButton) findViewById(R.id.buttonLogin);
        connectWithFacebookButton.setReadPermissions(permissions);
        connectWithFacebookButton.registerCallback(callbackManager, fbCallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
