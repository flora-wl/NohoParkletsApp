package org.artoolkit.ar.samples.ARSimpleNativeCars;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;


public class MainActivity extends Activity {

    final static String TAG = "Main";
    private final static int REQUEST_WRITE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView b1 = (ImageView) findViewById(R.id.infomenu);
        ImageView b2 = (ImageView) findViewById(R.id.notificationsmenu);
        ImageView b3 = (ImageView) findViewById(R.id.mapmenu);
        ImageView b4 = (ImageView) findViewById(R.id.parkletmenu);

        //Opens up smartparklet activity: displays data collected from parklet
        b4.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Intent maps = new Intent(MainActivity.this, DeviceList.class);
                startActivity(maps);
            }

        });

        //Opens up maps activity: displays interactive map
        b3.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Intent maps = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(maps);
            }

        });


        //Opens up information activity: displays information about the app
        b1.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Intent infoActivity = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(infoActivity);
            }

        });

        //Opens up profile activity: displays previous comments
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileActivity = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(profileActivity);
            }
        });

        //Checks for exceptions and asks for permission to write to external storage of phone
        //This is necessary for the app to run with ARToolkit and other unrelated activities
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    if (this.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        // Will drop in here if user denied permissions access camera before.
                        // Or no uses-permission CAMERA element is in the
                        // manifest file. Must explain to the end user why the app wants
                        // permissions to the camera devices.
                        Toast.makeText(this.getApplicationContext(),
                                "App requires access to write external storage to be granted",
                                Toast.LENGTH_SHORT).show();
                    }
                    // Request permission from the user to access the camera.
                    Log.i(TAG, "Presentation(): must ask user for write external storage access permission");
                    this.requestPermissions(new String[]
                                    {
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                                    },
                            REQUEST_WRITE);
                    return;
                }
            }
        } catch (Exception ex) {
            Log.e(TAG, "Presentation(): exception caught, " + ex.getMessage());
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionsResult(): called");
        if (requestCode == REQUEST_WRITE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(),
                        "Application will not run with folder access denied",
                        Toast.LENGTH_LONG).show();
            } else if (1 <= permissions.length) {
                Toast.makeText(getApplicationContext(),
                        String.format("Reading file access permission \"%s\" allowed", permissions[0]),
                        Toast.LENGTH_SHORT).show();
            }
        } else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

}

