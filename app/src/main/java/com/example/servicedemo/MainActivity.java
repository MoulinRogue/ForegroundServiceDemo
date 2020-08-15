package com.example.servicedemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button foreBtn;
    Button foreBtnThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        foreBtn = (Button) findViewById(R.id.startForeService);
        foreBtnThread = (Button) findViewById(R.id.startForeServiceThread);

        foreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startFSM();
            }
        });

        foreBtnThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startFST();
            }
        });
    }

    public void startFSM() {
        Intent serviceIntent = new Intent(this, ForegroundServiceMainUI.class);
        serviceIntent.putExtra("inputExtra", "Foreground Service in main UI Example in Android");
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void startFST() {
        Intent serviceIntent = new Intent(this, ForegroundServiceThread.class);
        serviceIntent.putExtra("inputExtra", "Foreground Service in new Thread Example in Android");
        ContextCompat.startForegroundService(this, serviceIntent);
    }

}