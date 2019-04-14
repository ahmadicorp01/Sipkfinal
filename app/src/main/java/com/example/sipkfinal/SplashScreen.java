package com.example.sipkfinal;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.newtronlabs.easypermissions.EasyPermissions;
import com.newtronlabs.easypermissions.listener.IError;
import com.newtronlabs.easypermissions.listener.IPermissionsListener;

import java.util.Set;

public class SplashScreen extends AppCompatActivity{
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

//        App app = new App(this);
//            app.showNotification("Versi baru tersedia!", "Ketuk untuk melakukan pembaruan aplikasi.", false);

        new Handler().postDelayed(new Runnable(){
            @Override
                    public void run(){
                Intent loginintent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(loginintent);
                finish();
            }
        },SPLASH_TIME_OUT);
    }

}
