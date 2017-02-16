package com.example.yu.login;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.*;


import static com.example.yu.login.R.id.gpsButton;

// The Privacy class handles GPS permissions. It can start and stop the GPS_Service.
public class Privacy extends AppCompatActivity {

    GoogleApiClient mGoogleClient;

    private ToggleButton gpsButton;
    private TextView textView;
    private BroadcastReceiver broadcastReceiver;

    // pre:
    // post: Creates broadcastReceiver that accepts intents with string "location_update".
    @Override
    protected void onResume() {
        super.onResume();
        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    textView.append("\n"+intent.getExtras().get("coordinates"));
                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("location_update"));
    }

    // pre:
    // post: Destroy broadcastReceiver after it is made.
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

    // pre:
    // post: Check permissions. Enable toggle if no runtime permissions needed.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        textView = (TextView) findViewById(R.id.textView);
        gpsButton = (ToggleButton) findViewById(R.id.gpsButton);

        if (!runtime_permissions()) {
            enable_buttons();
        }
    }

    // pre:
    // post: If toggle is on then start GPS. Else, stop GPS.
    private void enable_buttons() {
        gpsButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if (isChecked) {
                    Intent start = new Intent(getApplicationContext(), GPS_Service.class);
                    startService(start);
                } else {
                    Intent stop = new Intent(getApplicationContext(), GPS_Service.class);
                    stopService(stop);
                }
            }
        });
    }

    // pre:
    // post: Check runtime permissions. Return true if runtime permissions are needed. Else, return
    // false.
    private boolean runtime_permissions() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            return true;
        }
        return false;
    }

    // pre:
    // post: If permissions are granted then enable the toggle. Else, check for runtime permissions.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                enable_buttons();
            } else {
                runtime_permissions();
            }
        }
    }

}
