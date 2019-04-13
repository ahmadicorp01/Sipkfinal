package com.example.sipkfinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    int id_user = 0;
    SharedPreferences sharedPreferences;
    TextInputLayout text_username, text_password;
    Button btn_login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("SIPK", MODE_PRIVATE);
        id_user = sharedPreferences.getInt("id_user", 0);

        if (sharedPreferences.getBoolean("isLogin", false))
            openUtamaActivity();


        text_username = findViewById(R.id.username);
        text_password = findViewById(R.id.password);

        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateUsername() | !validatePassword()) {
                    return;
                }
                new UserLogin().execute(text_username.getEditText().getText().toString(), text_password.getEditText().getText().toString());
            }
        });
    }

    public void openUtamaActivity(){
        Intent intent = new Intent(this,UtamaActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean validateUsername() {
        String usernameInput = text_username.getEditText().getText().toString().trim();

        if (usernameInput.isEmpty()) {
            text_username.setError("Username wajib diisi");
            return false;
        } else {
            text_username.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = text_password.getEditText().getText().toString().trim();

        if (passwordInput.isEmpty()) {
            text_password.setError("Password wajib diisi");
            return false;
        } else {
            text_password.setError(null);
            return true;
        }
    }
    private class UserLogin extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObj = new JSONObject(s);

                if (jsonObj.getBoolean("status")) {
                    JSONObject jData = jsonObj.getJSONObject("data");

                    sharedPreferences.edit().putBoolean("isLogin", true).apply();
                    sharedPreferences.edit().putInt("id_user", jData.getInt("id_user")).apply();
                    sharedPreferences.edit().putInt("id_pengguna", jData.getInt("id_pengguna")).apply();
                    sharedPreferences.edit().putString("status", jData.getString("status")).apply();
                    sharedPreferences.edit().putInt("akses", jData.getInt("akses")).apply();

                    openUtamaActivity();

                }else
                    Toast.makeText(MainActivity.this, jsonObj.getString("data"), Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            HttpHandler sh = new HttpHandler();
            String url = "http://10.0.2.2/sipk/api/user_login/" + params[0] + "/" + params[1] ;
            String jsonStr = sh.makeServiceCall(url);

            return jsonStr;
        }
    }

}
