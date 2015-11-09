package it.hopapps.villaggiorock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.AccessController;
import java.security.Provider;
import java.security.Security;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class GadgetActivity extends AppCompatActivity {

    Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gadget);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
                alertDialogBuilder
                        .setTitle(R.string.gadget_dialog_title)
                        .setMessage(R.string.gadget_dialog_message)
                        .setCancelable(false)
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                GMailSender sender = new GMailSender("hopapps@gmail.com", "Hy6Ol.P0");
                                try {
                                    sender.sendMail(
                                            "Prova oggetto",
                                            "Prova corpo",
                                            "hopapps@gmail.com",
                                            "hopapps@gmail.com"
                                    );
                                    Log.println(1, "SendMessage", "Sono nel blocco try");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setPositiveButton("Sì", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    GMailSender sender = new GMailSender("hopapps@gmail.com", "Hy6Ol.P0");
                                    sender.sendMail(
                                            "Prova oggetto",
                                            "Prova corpo",
                                            "hopapps@gmail.com",
                                            "hopapps@gmail.com"
                                    );
                                } catch (Exception e) {
                                    Log.e("SendMail", e.getMessage());
                                }
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    public final class JSSEProvider extends Provider {

        public JSSEProvider() {
            super("HarmonyJSSE", 1.0, "Harmony JSSE Provider");
            AccessController.doPrivileged(new java.security.PrivilegedAction<Void>() {
                public Void run() {
                    put("SSLContext.TLS",
                            "org.apache.harmony.xnet.provider.jsse.SSLContextImpl");
                    put("Alg.Alias.SSLContext.TLSv1", "TLS");
                    put("KeyManagerFactory.X509",
                            "org.apache.harmony.xnet.provider.jsse.KeyManagerFactoryImpl");
                    put("TrustManagerFactory.X509",
                            "org.apache.harmony.xnet.provider.jsse.TrustManagerFactoryImpl");
                    return null;
                }
            });
        }
    }

    public class GMailSender extends javax.mail.Authenticator {
        private String mailhost = "smtp.gmail.com";
        private String user;
        private String password;
        private Session session;

        {
            Security.addProvider(new JSSEProvider());
        }

        public GMailSender(String user, String password) {
            this.user = user;
            this.password = password;

            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", "smtp");
            props.setProperty("mail.host", mailhost);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class",
                    "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.fallback", "false");
            props.setProperty("mail.smtp.quitwait", "false");

            session = Session.getDefaultInstance(props, this);
        }

        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(user, password);
        }

        public synchronized void sendMail(String subject, String body, String sender, String recipients) throws Exception {
            try{
                MimeMessage message = new MimeMessage(session);
                DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
                message.setSender(new InternetAddress(sender));
                message.setSubject(subject);
                message.setDataHandler(handler);
                if (recipients.indexOf(',') > 0)
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
                else
                    message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
                Transport.send(message);
            }catch(Exception e){
                Log.e("MailSender", "");
            }
        }

        public class ByteArrayDataSource implements DataSource {
            private byte[] data;
            private String type;

            public ByteArrayDataSource(byte[] data, String type) {
                super();
                this.data = data;
                this.type = type;
            }

            public ByteArrayDataSource(byte[] data) {
                super();
                this.data = data;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getContentType() {
                if (type == null)
                    return "application/octet-stream";
                else
                    return type;
            }

            public InputStream getInputStream() throws IOException {
                return new ByteArrayInputStream(data);
            }

            public String getName() {
                return "ByteArrayDataSource";
            }

            public OutputStream getOutputStream() throws IOException {
                throw new IOException("Not Supported");
            }
        }
    }
}
