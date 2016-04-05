package com.memoid.workreminder.activity;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.memoid.workreminder.R;
import com.memoid.workreminder.service.MailService;

public class DailyMailActivity extends AppCompatActivity {

    private String USER;
    private String PASSWORD;
    private PendingIntent pendingIntent;

    FloatingActionButton fab;
    EditText todayText, tomorrowText, confidenceText;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                int resultCode = bundle.getInt("result");
                if (resultCode == RESULT_OK)
                    Toast.makeText(context.getApplicationContext(), "Mail sent", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(context.getApplicationContext(), "Error on sending mail", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        USER = getString(R.string.user);
        PASSWORD = getString(R.string.password);
        setContentView(R.layout.activity_daily_mail);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        todayText = (EditText) findViewById(R.id.todayText);
        tomorrowText = (EditText) findViewById(R.id.tomorrowText);
        confidenceText = (EditText) findViewById(R.id.confidenceText);
        Intent intent = new Intent(this, MailService.class);
        pendingIntent = PendingIntent.getBroadcast(DailyMailActivity.this, 0, intent, 0);
    }

    public void sendMail(View view) {

        String body = "Today: \n" +
                todayText.getText().toString() + "\n\n\n" +
                "Tomorrow: \n" +
                tomorrowText.getText().toString() + "\n\n\n" +
                "Confidence: \n" +
                confidenceText.getText().toString();

        String[] tos = {USER};

        Bundle bundle = new Bundle();
        bundle.putString("body", body);
        bundle.putString("user", USER);
        bundle.putString("password", PASSWORD);
        bundle.putStringArray("tos", tos);
        bundle.putString("from", USER);
        bundle.putString("subject", "Daily Report");

        Intent intent = new Intent(this, MailService.class);
        intent.putExtras(bundle);
        startService(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(MailService.NOTIFICATION));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
