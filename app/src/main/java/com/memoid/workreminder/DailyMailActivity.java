package com.memoid.workreminder;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.view.View;
import android.widget.EditText;

import com.memoid.workreminder.util.Mail;

public class DailyMailActivity extends AppCompatActivity {

    private String USER;
    private String PASSWORD;
    FloatingActionButton fab;
    EditText todayText, tomorrowText, confidenceText;

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
    }

    public void sendMail(View view) {

        StringBuilder body = new StringBuilder();
        body.append("Today: \n");
        body.append(todayText.getText().toString() + "\n\n\n");
        body.append("Tomorrow: \n");
        body.append(tomorrowText.getText().toString() + "\n\n\n");
        body.append("Confidence: \n");
        body.append(confidenceText.getText().toString());

        Mail m = new Mail(USER, PASSWORD);
        String[] tos = {USER};
        m.setTo(tos);
        m.setFrom(USER);
        m.setSubject("Daily Report");
        m.setBody(body.toString());

        Thread myThread = new Thread(m);
        myThread.start();

    }
}
