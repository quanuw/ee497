package com.example.yu.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MenuActivity_v2 extends AppCompatActivity {
    private Button gpsSettings;

    // pre:
    // post: "gps settings" button leads to privacy activity.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_v2);
        gpsSettings = (Button) findViewById(R.id.gpsSettings);
        gpsSettings.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v) {
                        Intent privacyIntent = new Intent(MenuActivity_v2.this, Privacy.class);
                        MenuActivity_v2.this.startActivity(privacyIntent);
                    }
                } );
    }

}