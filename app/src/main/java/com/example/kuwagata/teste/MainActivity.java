package com.example.kuwagata.teste;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.kuwagata.teste.services.ChangeTextViewMessage;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d("Receiver", "Estou executando");

            if (intent.getAction().equals("br.com.interaje.teste.message")) {
                textView.setText(intent.getStringExtra("message"));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.message_textview);

        registerReceiver(mReceiver, new IntentFilter("br.com.interaje.teste.message"));

        startService(new Intent(this, ChangeTextViewMessage.class));

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_default);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
