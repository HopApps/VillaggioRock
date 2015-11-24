package it.hopapps.villaggiorock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;
import it.hopapps.villaggiorock.adapters.RVMenuAdapter;
import it.hopapps.villaggiorock.models.CustomMenuItem;

public class MenuActivity extends AppCompatActivity {

    private List<CustomMenuItem> menuItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
        setContentView(R.layout.activity_menu);
        initializeMenuItems();

        RecyclerView rv = (RecyclerView) findViewById(R.id.recicler_view_activity_menu);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        RVMenuAdapter adapter = new RVMenuAdapter(menuItems, MenuActivity.this);
        rv.setAdapter(adapter);
        rv.setHasFixedSize(true);

    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                return true;

            case R.id.action_credits:
                showCredits();
                return true;

        }
        return false;
    }

    private void initializeMenuItems(){
        menuItems = new ArrayList<>();
        menuItems.add(new CustomMenuItem(getResources().getString(R.string.events_menu_name), R.drawable.menu_item_events));
        menuItems.add(new CustomMenuItem(getResources().getString(R.string.photos_menu_name), R.drawable.menu_item_foto));
        menuItems.add(new CustomMenuItem(getResources().getString(R.string.playlist_menu_name), R.drawable.menu_item_playlist));
        //menuItems.add(new MenuItem(getResources().getString(R.string.reservation_menu_name), R.drawable.menu_item_prenotazione));
        menuItems.add(new CustomMenuItem(getResources().getString(R.string.gadget_menu_name), R.drawable.menu_item_gadget));
        menuItems.add(new CustomMenuItem(getResources().getString(R.string.live_menu_name), R.drawable.menu_item_live));
    }

    private void logout () {
        Context ctx = this;
        AlertDialog.Builder alertDialogBuilderLogout = new AlertDialog.Builder(ctx);
        alertDialogBuilderLogout
                .setTitle(ctx.getString(R.string.action_logout))
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface creditDialog, int which) {
                        creditDialog.cancel();
                    }
                })
                .setPositiveButton("Sì", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LoginManager.getInstance().logOut();
                    }
                });
        AlertDialog logoutAlertDialog = alertDialogBuilderLogout.create();
        logoutAlertDialog.show();
    }

    private void showCredits () {
        Context ctx = this;
        AlertDialog.Builder alertDialogBuilderCredits = new AlertDialog.Builder(ctx);

        LayoutInflater inflater = this.getLayoutInflater();

        alertDialogBuilderCredits
                .setTitle(ctx.getString(R.string.action_credits))
                .setView(inflater.inflate(R.layout.credits_dialog, null))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface creditDialog, int which) {
                        creditDialog.cancel();
                    }
                });
        AlertDialog creditsAlertDialog = alertDialogBuilderCredits.create();
        creditsAlertDialog.show();
    }
}
