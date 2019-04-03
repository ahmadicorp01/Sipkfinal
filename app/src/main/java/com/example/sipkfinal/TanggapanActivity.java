package com.example.sipkfinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class TanggapanActivity extends AppCompatActivity {
    private RecyclerView cRecyclerView;
    private ChatAdapter cAdapter;
    private RecyclerView.LayoutManager cLayoutManager;

    EditText tanggapan;
    TextView text_keluhan, text_status;
    Button kirim, arrow_back;
    int id_user = 0;
    SharedPreferences sharedPreferences;
    private ArrayList<ChatItem> chatItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tanggapan);

        sharedPreferences = getSharedPreferences("SIPK", MODE_PRIVATE);
        id_user = sharedPreferences.getInt("id_user", 0);

        text_keluhan = findViewById(R.id.text_keluhan);
        text_keluhan.setMovementMethod(new ScrollingMovementMethod());
        text_status = findViewById(R.id.text_status);

        Intent intent = getIntent();
        final String id_laporan = intent.getExtras().getString("id_laporan");

        tanggapan = findViewById(R.id.text_pesan);
        arrow_back = findViewById(R.id.arrow_back);
        arrow_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openDaftarActivity();
            }
        });
        kirim = findViewById(R.id.btn_kirim);
        kirim.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new sendChat().execute(Integer.toString(id_user),
                        id_laporan,
                        tanggapan.getText().toString());
                recreate();
                tanggapan.setText("");
            }
        });

        chatItems = new ArrayList<>();
        cRecyclerView = findViewById(R.id.recycler_chat);

        cLayoutManager = new LinearLayoutManager(this);
        cAdapter = new ChatAdapter(chatItems);

        cRecyclerView.setLayoutManager(cLayoutManager);
        cRecyclerView.setAdapter(cAdapter);

        new UserKeluhan().execute(id_laporan);
        new UserChatDaftar().execute(id_laporan);


    }
    public void openDaftarActivity() {
        Intent intent = new Intent(this, DaftarActivity.class);
        startActivity(intent);
    }

    private class UserChatDaftar extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String s) {
            try {
//                Log.d("SIPK", s);

                JSONObject jsonObj = new JSONObject(s);

                if (jsonObj.getBoolean("status")) {
                    JSONArray jData = jsonObj.getJSONArray("data");

                    for (int i = 0; i < jData.length(); i++) {
                        JSONObject c = jData.getJSONObject(i);

                        chatItems.add(new ChatItem(c.getString("nama_pengguna"), c.getString("tanggapan"), c.getString("waktu")));
                        cAdapter.notifyDataSetChanged();
                    }

                } else
                    Toast.makeText(TanggapanActivity.this, jsonObj.getString("data"), Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            HttpHandler sh = new HttpHandler();
            String url = "http://10.0.2.2/sipk/api/user_tanggapan/" + params[0];
            String jsonStr = sh.makeServiceCall(url);

            return jsonStr;
        }
    }

    private class UserKeluhan extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObj = new JSONObject(s);

                if (jsonObj.getBoolean("status")) {
                    JSONObject c = jsonObj.getJSONObject("data");
//                    Log.d("SIPK", s);

                    text_keluhan.setText(c.getString("keluhan"));
                    text_status.setText(c.getString("laporan_status"));

                    if(c.getInt("laporan_status")==1){
                        text_status.setText("Pending");
                    }if (c.getInt("laporan_status") == 2){
                        text_status.setText("Proses");
                    }if(c.getInt("laporan_status")==3){
                        text_status.setText("Selesai");
                    }


                } else
                    Toast.makeText(TanggapanActivity.this, jsonObj.getString("data"), Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            HttpHandler sh = new HttpHandler();
            String url = "http://10.0.2.2/sipk/api/user_parsing_keluhan/" + params[0];
            String jsonStr = sh.makeServiceCall(url);

            return jsonStr;
        }
    }

    private class sendChat extends AsyncTask <String,Void,String>{
        @Override
        protected String doInBackground(String... params) {
            String reg_url = "http://10.0.2.2/sipk/api/user_tanggapan_kirim/";
            String text = "";

            Log.d("SIPK", reg_url);

            String id_pengguna = params[0];
            String id_laporan = params[1];
            String tanggapan = params[2];
            String dibaca = "1";

            try{
                URL url = new URL(reg_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();

                        String data = URLEncoder.encode("id_pengguna","UTF-8") + "=" + URLEncoder.encode(id_pengguna, "UTF-8") + "&" +
                                URLEncoder.encode("id_laporan","UTF-8") + "=" + URLEncoder.encode(id_laporan, "UTF-8") + "&" +
                        URLEncoder.encode("tanggapan", "UTF-8") + "=" +  URLEncoder.encode(tanggapan,"UTF-8") + "&" +
                        URLEncoder.encode("dibaca", "UTF-8") + "=" +  URLEncoder.encode(dibaca,"UTF-8");


                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                bufferedWriter.write(data);
                bufferedWriter.flush();

                int statusCode = httpURLConnection.getResponseCode();
                Log.d("SIPK", "Status: " + statusCode);

                if(statusCode == 200){
                    BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while (((line = reader.readLine()))!=null)
                        sb.append(line).append("\n");
                    text = sb.toString();
                    bufferedWriter.close();
                }
            }
            catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            return text;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("SIPK", result);

//            try{
//                JSONObject obj = new JSONObject(result);
//                Toast.makeText(LaporActivity.this, obj.getString("data"), Toast.LENGTH_SHORT).show();
//            }catch (JSONException e) {
//                e.printStackTrace();
//            }
        }
    }
}
