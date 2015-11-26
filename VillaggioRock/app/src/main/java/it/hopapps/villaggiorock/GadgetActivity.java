package it.hopapps.villaggiorock;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.kristijandraca.backgroundmaillibrary.BackgroundMail;

import it.hopapps.villaggiorock.asyncTasks.FacebookGadgetRetriever;

public class GadgetActivity extends AppCompatActivity {
    private Context ctx = this;
    private String gadgetName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gadget);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        Intent thisIntent = this.getIntent();
        gadgetName = thisIntent.getExtras().getString("gadget_name");
        collapsingToolbar.setTitle(gadgetName);
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        int gadgetImage = thisIntent.getExtras().getInt("gadget_image");
        appBarLayout.setBackground(ContextCompat.getDrawable(this, gadgetImage));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new FacebookGadgetRetriever(this).execute();

        String[] sizeOptions = new String[] { ctx.getString(R.string.gadget_size_spinner_header), "XS", "S", "M", "L", "XL", "XXL" };
        ArrayAdapter<String> sizeAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, sizeOptions);
        final Spinner sizeSpinner = (Spinner) findViewById(R.id.size_spinner);
        sizeSpinner.setAdapter(sizeAdapter);

        String[] colourOptions = new String[] {ctx.getString(R.string.gadget_colour_spinner_header), "Bianco", "Nero"};
        ArrayAdapter<String> colourAdapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item,
                colourOptions
        );
        final Spinner colourSpinner = (Spinner) findViewById(R.id.colour_spinner);
        colourSpinner.setAdapter(colourAdapter);

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
                alertDialogBuilder
                        .setTitle(R.string.gadget_dialog_title)
                        .setMessage(R.string.gadget_dialog_message)
                        .setCancelable(false)
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("Sì", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                BackgroundMail backgroundMail = new BackgroundMail(ctx, ((Activity)ctx).findViewById(R.id.gadget_layout_coordinator));
                                backgroundMail.setGmailUserName(ctx.getString(R.string.hopapps_mail));
                                backgroundMail.setGmailPassword(ctx.getString(R.string.hopapps_pwd));
                                backgroundMail.setMailTo(ctx.getString(R.string.hopapps_mail));
                                backgroundMail.setFormSubject(ctx.getString(R.string.hopapps_gadget_mail_subject));

                                final TextView hiddenName = (TextView) findViewById(R.id.gadget_tv_name_hidden);
                                final EditText mailEditText = (EditText) findViewById(R.id.gadget_et_mail);

                                String body = reservationMailBodyFormatter(sizeSpinner, colourSpinner, hiddenName, mailEditText);

                                if (body == null) {
                                    AlertDialog.Builder alertDialogBuilderError = new AlertDialog.Builder(ctx);
                                    alertDialogBuilderError
                                            .setTitle(ctx.getString(R.string.missing_fields_error_dialog_title))
                                            .setMessage(ctx.getString(R.string.missing_fields_error_dialog_body))
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface errorDialog, int which) {
                                                    errorDialog.cancel();
                                                }
                                            });
                                    AlertDialog errorAlertDialog = alertDialogBuilderError.create();
                                    errorAlertDialog.show();
                                    dialog.cancel();
                                }

                                else {
                                    backgroundMail.setFormBody(body);
                                    backgroundMail.send();
                                }
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    protected String reservationMailBodyFormatter (Spinner sSpinner, Spinner cSpinner, TextView hiddenName, EditText mailEditText) {

        if (sSpinner.getSelectedItem() == ctx.getString(R.string.gadget_size_spinner_header)
            || cSpinner.getSelectedItem() == ctx.getString(R.string.gadget_colour_spinner_header)
            || mailEditText.getText().length() == 0) {
            return null;
        }

        return ctx.getString(R.string.gadget_mail_body_header_1)
                + " " + hiddenName.getText() + " "
                + ctx.getString(R.string.gadget_mail_body_header_2)
                + " " + gadgetName + " "
                + ctx.getString(R.string.gadget_mail_body_header_3)
                + ctx.getString(R.string.gadget_mail_body_size)
                + " " + sSpinner.getSelectedItem().toString()
                + ctx.getString(R.string.gadget_mail_body_colour)
                + " " + cSpinner.getSelectedItem()
                + ctx.getString(R.string.gadget_mail_body_mail_address)
                + " " + mailEditText.getText().toString();
    }
}
