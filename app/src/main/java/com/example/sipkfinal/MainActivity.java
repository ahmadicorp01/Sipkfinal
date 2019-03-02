package com.example.sipkfinal;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
    }

    public class UserLogin extends AsyncTask<String, Void, String> {
        String message = "message";
        String loginSuccess;
        String Id = "Id";
    }

    
}
