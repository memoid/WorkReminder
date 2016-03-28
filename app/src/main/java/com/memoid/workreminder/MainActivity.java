package com.memoid.workreminder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    MailReceiver mailAlarm = new MailReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mailAlarm.setAlarm(this);
    }

    public void toDailyMail(View view) {
        Intent intent = new Intent(this, DailyMailActivity.class);
        startActivity(intent);
    }
}
