//package com.example.sipkfinal;
//
//import android.app.Service;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.AsyncTask;
//import android.os.Handler;
//import android.os.IBinder;
//import android.support.annotation.Nullable;
//import android.util.Log;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.Timer;
//import java.util.TimerTask;
//
//public class InternetService extends Service {
//    public static final long NOTIFY_INTERVAL = 10 * 1000; // 10 seconds
//
//    // run on another Thread to avoid crash
//    private Handler mHandler = new Handler();
//    private Timer mTimer = null;
//    private String id_user = null;
//    private App core = null;
//    private SharedPreferences sp;
//
//    @Override
//    public void onCreate() {
//        if (mTimer != null)
//            mTimer.cancel();
//        else
//            mTimer = new Timer();
//
//        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
//    }
//
//    class TimeDisplayTimerTask extends TimerTask {
//
//        @Override
//        public void run() {
//            mHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    if (core.cekInternet())
//                        new getNotifikasi().execute(id_user);
//                    else
//                        Log.d("SIPK", "No Internet");
//                }
//            });
//        }
//    }
//
//    class getNotifikasi extends AsyncTask<String,String,String>{
//
//        JSONObject jobj = null;
//
//        @Override
//        protected String doInBackground(String... params) {
//            JSONParser jsonparser = new JSONParser();
//
//            String url = core.API("notifikasi/" + params[0]);
//            jobj = jsonparser.makeHttpRequest(url);
//
//            return jobj.toString();
//        }
//
//        protected void onPostExecute(String json){
//            try {
//                JSONObject jobj = new JSONObject(json);
//                JSONArray jdata = jobj.getJSONArray("data");
//
//                for (int i = 0; i < jdata.length(); i++) {
//                    JSONObject c = jdata.getJSONObject(i);
//
//                    core.showNotification(c.getString("judul"), c.getString("pesan"));
//                }
//
//
//            } catch (JSONException e) {
//                Log.d("SIPK", e.getMessage());
//
//            }
//        }
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        super.onStartCommand(intent, flags, startId);
//        core = new App(getApplicationContext());
//
//        sp = getSharedPreferences("SIPK", MODE_PRIVATE);
//        id_user = String.valueOf(sp.getInt("id_user", 0));
//
//        return START_STICKY;
//    }
//}