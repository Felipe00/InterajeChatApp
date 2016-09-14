package com.example.kuwagata.teste.chat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kuwagata.teste.R;
import com.example.kuwagata.teste.chat.model.ChatUser;

public class HomeActivity extends AppCompatActivity {

    private EditText edtxtName, edtxtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        edtxtEmail = (EditText) findViewById(R.id.etxt_email);
        edtxtName = (EditText) findViewById(R.id.etxt_name);
    }

    /**
     * Verifica se está preenchido os 2 campos.Se estiver, manda os 2 campos para a ChatActivity, senão mostra um toast.
     * @param v
     */
    public void connect(View v) {
        if (!edtxtName.getText().toString().equalsIgnoreCase("") || !edtxtEmail.getText().toString().equalsIgnoreCase("")) {
            ChatUser chatUser = new ChatUser(edtxtEmail.getText().toString(), edtxtName.getText().toString());
            startActivity(new Intent(HomeActivity.this, ChatActivity.class).putExtra("chatUser", chatUser));
            finish();
        } else {
            Toast.makeText(HomeActivity.this, R.string.toast_home_fill_all_fields, Toast.LENGTH_SHORT).show();
        }
    }

}
