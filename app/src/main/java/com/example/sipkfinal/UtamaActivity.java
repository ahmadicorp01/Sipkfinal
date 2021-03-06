package com.example.sipkfinal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class UtamaActivity extends AppCompatActivity {
    int id_user = 0;
    SharedPreferences sharedPreferences;
    TextView text_nama, text_perusahaan;
    CardView btn_lapor, btn_lapor_daftar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utama);

        sharedPreferences = getSharedPreferences("SIPK", MODE_PRIVATE);
        id_user = sharedPreferences.getInt("id_user", 0);
        new UserDetail().execute(Integer.toString(id_user));

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        text_nama = findViewById(R.id.text_nama);
        text_perusahaan = findViewById(R.id.text_perusahaan);

        btn_lapor = findViewById(R.id.btn_lapor);
        btn_lapor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLaporActivity();
            }
        });


        btn_lapor_daftar = findViewById(R.id.btn_lapor_daftar);
        btn_lapor_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDaftarActivity();
            }
        });
    }

    public void openDaftarActivity() {
        Intent intent = new Intent(this, DaftarActivity.class);
        startActivity(intent);
    }

    public void openLaporActivity() {
        Intent intent = new Intent(this, LaporActivity.class);
        startActivity(intent);
    }

    private class UserDetail extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObj = new JSONObject(s);

                if (jsonObj.getBoolean("status")) {
                    JSONObject c = jsonObj.getJSONObject("data");

                    text_nama.setText(c.getString("nama_pengguna"));
                    text_perusahaan.setText(c.getString("perusahaan"));

                } else
                    Toast.makeText(UtamaActivity.this, jsonObj.getString("data"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            HttpHandler sh = new HttpHandler();
            String url = "http://10.0.2.2/sipk/api/user_detail/" + params[0];
            String jsonStr = sh.makeServiceCall(url);

            return jsonStr;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_logout:
                SharedPreferences sharedPreferences = getSharedPreferences("SIPK", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();

                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);

                finish();

                default:
                    return super.onOptionsItemSelected(item);
        }
    }
}
