package com.example.sipkfinal;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DaftarActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private LaporanAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    int id_user = 0;
    SharedPreferences sharedPreferences;
    private ArrayList<LaporanItem> laporanItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        sharedPreferences = getSharedPreferences("SIPK", MODE_PRIVATE);
        id_user = sharedPreferences.getInt("id_user", 0);

        laporanItems = new ArrayList<>();
        mRecyclerView = findViewById(R.id.recycler_view);

//        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new LaporanAdapter(laporanItems);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener(new LaporanAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                changeItem(position,"clicked");
            }
        });

        new UserLaporanDaftar().execute(Integer.toString(id_user));
    }

    public void changeItem(int position, String text){
        laporanItems.get(position).changeText1(text);
        mAdapter.notifyItemChanged(position);
    }

    private class UserLaporanDaftar extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObj = new JSONObject(s);

                if (jsonObj.getBoolean("status")) {
                    JSONArray jData = jsonObj.getJSONArray("data");

                    for (int i = 0; i < jData.length(); i++) {
                        JSONObject c = jData.getJSONObject(i);

                        Log.d("SIPK", c.getString("judul_keluhan"));
                        laporanItems.add(new LaporanItem(c.getString("judul_keluhan"), c.getString("nama_kategori"),c.getString("waktu")));
                        mAdapter.notifyDataSetChanged();
                    }

                }
                else
                    Toast.makeText(DaftarActivity.this, jsonObj.getString("data"), Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpHandler sh = new HttpHandler();
            String url = "http://10.0.2.2/sipk/api/user_lapor_daftar/" + strings[0];
            String jsonStr = sh.makeServiceCall(url);

            return jsonStr;
        }
    }
}
