package it.hopapps.villaggiorock;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.kristijandraca.backgroundmaillibrary.BackgroundMail;

import it.hopapps.villaggiorock.asyncTasks.FacebookReservationRetriever;

public class ReservationActivity extends AppCompatActivity {

    Context ctx = this;
    String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = this.getIntent();
        eventId = i.getExtras().getString("id");

        setContentView(R.layout.activity_reservation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String[] reservationOptions = new String[] {
                ctx.getString(R.string.reservation_spinner_header), "Tavolo", "Laurea"
        };

        final Spinner reservationSpinner = (Spinner) findViewById(R.id.reservation_type_spinner);

        ArrayAdapter<String> reservationAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                reservationOptions
        );
        reservationSpinner.setAdapter(reservationAdapter);

        final EditText goingEditText = (EditText) findViewById(R.id.et_going);
        final EditText mailEditText = (EditText) findViewById(R.id.et_mail);
        final TextView hiddenName = (TextView) findViewById(R.id.tv_name_hidden);

        FacebookReservationRetriever fbrr = new FacebookReservationRetriever(eventId, this);
        fbrr.execute();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
                alertDialogBuilder
                        .setTitle(R.string.reservation_dialog_title)
                        .setMessage(R.string.reservation_dialog_body)
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
                                BackgroundMail backgroundMail = new BackgroundMail(ctx, ((Activity)ctx).findViewById(R.id.reservation_layout_coordinator));
                                backgroundMail.setGmailUserName(ctx.getString(R.string.hopapps_mail));
                                backgroundMail.setGmailPassword(ctx.getString(R.string.hopapps_pwd));
                                backgroundMail.setMailTo(ctx.getString(R.string.hopapps_mail));
                                backgroundMail.setFormSubject(ctx.getString(R.string.hopapps_reservation_mail_subject));

                                String body = reservationMailBodyFormatter(
                                        reservationSpinner,
                                        goingEditText,
                                        mailEditText,
                                        hiddenName
                                );
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

    protected String reservationMailBodyFormatter (Spinner rSpinner, EditText goingEditText, EditText mailEditText, TextView hiddenName) {

        if (rSpinner.getSelectedItem() == ctx.getString(R.string.reservation_spinner_header)
                || goingEditText.getText().toString().isEmpty()
                || mailEditText.getText().toString().isEmpty()) {
            return null;
        }

        return ctx.getString(R.string.reservation_mail_body_header_one)
                + " " + hiddenName.getText() + " "
                + ctx.getString(R.string.reservation_mail_body_header_two)
                + ctx.getString(R.string.reservation_mail_body_type)
                + " " + rSpinner.getSelectedItem().toString()
                + ctx.getString(R.string.reservation_mail_body_going)
                + " " + goingEditText.getText().toString()
                + ctx.getString(R.string.reservation_mail_body_mail_address)
                + " " + mailEditText.getText().toString();
    }
}