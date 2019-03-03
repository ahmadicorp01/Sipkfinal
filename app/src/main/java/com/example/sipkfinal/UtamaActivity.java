package com.example.sipkfinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UtamaActivity extends AppCompatActivity {
    Button btn_lapor,btn_lapor_daftar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utama);

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
    public void openDaftarActivity(){
        Intent intent = new Intent(this,DaftarActivity.class);
        startActivity(intent);
    }

    public void openLaporActivity(){
        Intent intent = new Intent(this,LaporActivity.class);
        startActivity(intent);
    }
}
