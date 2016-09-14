package com.example.kuwagata.teste.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by kuwagata on 12/09/16.
 */
public class ChangeTextViewMessage extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public ChangeTextViewMessage() {
        super("ChangeTextViewMessage");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Faça sua atividade aqui

        Log.d("Service", "Estou executando");

        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Intent messageIntent = new Intent("br.com.interaje.teste.message");
        messageIntent.putExtra("message", "Olá Pessoal do Interaje");
        sendBroadcast(messageIntent);
    }
}
