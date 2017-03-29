package org.artoolkit.ar.samples.ARSimpleNativeCars;

        import android.app.Dialog;
        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.Locale;
        import java.util.TimeZone;

public class NotificationView extends AppCompatActivity {

    public static String mycomment = "my comment";
    public static ArrayList<String> commentList = new ArrayList<String>();
    private static SimpleDateFormat df=new SimpleDateFormat("MM-dd-yyyy' at 'HH:mm", Locale.US);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_view);

        ImageView b = (ImageView) findViewById(R.id.ar);

        df.setTimeZone(TimeZone.getTimeZone("GMT-5"));
        final String date = df.format(new Date());

        //Opens up AR activity when button is pressed
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent arActivity = new Intent(NotificationView.this, ARSimpleNativeCarsActivity.class);
                startActivity(arActivity);
            }
        });


        ImageView btnlike = (ImageView) findViewById(R.id.likebutton);
        ImageView showDialog = (ImageView) findViewById(R.id.commentbutton);

        final TextView userinputtext = (TextView) findViewById(R.id.userinputtext);

        showDialog.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(NotificationView.this);
                dialog.setContentView(R.layout.prompts);

                Button button = (Button) dialog.findViewById(R.id.dialog_ok);
                button.setOnClickListener(new View.OnClickListener() {

                    //When comment button is pressed, comment is stored to ProfileActivity
                    @Override
                    public void onClick(View view) {
                        EditText userInput = (EditText) dialog.findViewById(R.id.userinput);
                        String comment = userInput.getText().toString()+" \n\n           - Cracker Barrel Alley \n             "+date;
                        userinputtext.setText(userInput.getText().toString());

                        Intent i = new Intent(view.getContext(), ProfileActivity.class );
                        i.putExtra(mycomment, comment);

                        startActivity(i);

                        dialog.dismiss();
                    }

                });

                dialog.show();
                Toast.makeText(getBaseContext(), "Thank you for your feedback" , Toast.LENGTH_SHORT ).show();

            }


        });

        //When like button is pressed, a toast is sent to the user
        btnlike.setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(android.view.View v)  {
                Toast.makeText(getBaseContext(), "You liked our idea!" , Toast.LENGTH_SHORT ).show();
            }
        });
    }
}
