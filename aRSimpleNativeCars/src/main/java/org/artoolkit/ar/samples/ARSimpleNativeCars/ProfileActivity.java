package org.artoolkit.ar.samples.ARSimpleNativeCars;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ProfileActivity extends AppCompatActivity {


    public static ArrayList<String> addArray = new ArrayList<String>();

    ListView listview;

    int imageId = R.drawable.comments1;
    int imageId2 = R.drawable.ecoparklet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        listview = (ListView) findViewById(R.id.listview_comments);

        Intent intent = getIntent();

        String userInput = intent.getStringExtra(NotificationView.mycomment);

        addArray.add(userInput);


        CustomListAdapter adapter = new CustomListAdapter(ProfileActivity.this, addArray, imageId, imageId2);

        listview.setAdapter(adapter);


    }
}