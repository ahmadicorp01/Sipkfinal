package com.example.sipkfinal;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Timer;
import java.util.TimerTask;

public class InternetService extends Service {
    public static final long NOTIFY_INTERVAL = 10 * 1000; // 10 seconds

    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    private String id_user = null;
    private App app = null;
    private SharedPreferences sp;

    @Override
    public void onCreate() {
        if (mTimer != null)
            mTimer.cancel();
        else
            mTimer = new Timer();

        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
    }

    class TimeDisplayTimerTask extends TimerTask {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    // if (app.cekInternet())
                        new getNotifikasi().execute(id_user);
//                    app.showNotification("Pesan baru dari Admin", "Hahahhahaha");
//                    else
//                        Log.d("SIPK", "No Internet");
                }
            });
        }

    }
    class getNotifikasi extends AsyncTask<String,String,String>{
        @Override
        protected void onPostExecute(String json){
            try {
                JSONObject jobj = new JSONObject(json);
                JSONObject c = jobj.getJSONObject("data");

                Log.d("SIPK", c.getString("tanggapan"));
                app.showNotification("Pesan baru dari Admin", c.getString("tanggapan"));

            } catch (JSONException e) {
                Log.d("SIPK", e.getMessage());
            }
        }

        @Override
        protected String doInBackground(String... params) {
            HttpHandler sh = new HttpHandler();
            String url = "http://10.0.2.2/sipk/api/user_notif/" + params[0];
            String jsonStr = sh.makeServiceCall(url);

            return jsonStr;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        app = new App(getApplicationContext());

        sp = getSharedPreferences("SIPK", MODE_PRIVATE);
        id_user = String.valueOf(sp.getInt("id_user", 0));

        return START_STICKY;
    }
}