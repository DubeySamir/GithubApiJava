package com.sdcode.githubapi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button btn_login;
    private EditText inputUserName;
    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_login = findViewById(R.id.btn_login);
        inputUserName = findViewById(R.id.input_username);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUser(v);
            }
        });
    }

    public void getUser(View view) {

        i = new Intent(MainActivity.this, UserActivity.class);
        i.putExtra("STRING_I_NEED", inputUserName.getText().toString());
        startActivity(i);
    }
}