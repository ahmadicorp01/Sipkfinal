package com.example.sipkfinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TanggapanActivity extends AppCompatActivity {
    private RecyclerView cRecyclerView;
    private ChatAdapter cAdapter;
    private RecyclerView.LayoutManager cLayoutManager;

    int id_user = 0;
    SharedPreferences sharedPreferences;
    private ArrayList<ChatItem> chatItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tanggapan);

        sharedPreferences = getSharedPreferences("SIPK", MODE_PRIVATE);
        id_user = sharedPreferences.getInt("id_user", 0);

        chatItems = new ArrayList<>();
        cRecyclerView = findViewById(R.id.recycler_chat);

        cLayoutManager = new LinearLayoutManager(this);
        cAdapter = new ChatAdapter(chatItems);

        cRecyclerView.setLayoutManager(cLayoutManager);
        cRecyclerView.setAdapter(cAdapter);

        Intent intent = getIntent();
        String id_laporan = intent.getExtras().getString("id_laporan");

        new UserChatDaftar().execute(id_laporan);

    }

    private class UserChatDaftar extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String s) {
            try {
                Log.d("SIPK", s);

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
        protected String doInBackground(String... strings) {
            HttpHandler sh = new HttpHandler();
            String url = "http://10.0.2.2/sipk/api/user_tanggapan/" + strings[0];
            String jsonStr = sh.makeServiceCall(url);

            return jsonStr;
        }
    }
}
