package com.example.sipkfinal;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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

public class LaporActivity extends AppCompatActivity {

    int id_user = 0;
    SharedPreferences sharedPreferences;
    EditText judul_keluhan,keluhan;
    Spinner spinner;
    Button btn_lapor_tambah,button2;
    ArrayList<String> myList1,myList2;
    private ArrayAdapter<String>myAdapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lapor);

        sharedPreferences = getSharedPreferences("SIPK", MODE_PRIVATE);
        id_user = sharedPreferences.getInt("id_user", 0);

        judul_keluhan = findViewById(R.id.judul_keluhan);
        keluhan = findViewById(R.id.keluhan);
        button2 = findViewById(R.id.button2);

        spinner = findViewById(R.id.kategori_spinner);
        myList1 = new ArrayList<>();
        myList2 = new ArrayList<>();

        myAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, myList1);
        myAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(myAdapter1);

        btn_lapor_tambah = findViewById(R.id.btn_lapor_tambah);
        btn_lapor_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String kategori = myList2.get(spinner.getSelectedItemPosition());
                Log.d("SIPK", kategori);
                new sendLaporan().execute(Integer.toString(id_user),
                        judul_keluhan.getText().toString(),
                        kategori,
                        keluhan.getText().toString());
            }
        });

        new UserKategoriDaftar().execute(Integer.toString(id_user));
    }

    private class UserKategoriDaftar extends AsyncTask<String, String, String> {
        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObj = new JSONObject(s);

                if (jsonObj.getBoolean("status")) {
                    JSONArray jData = jsonObj.getJSONArray("data");

                    for (int i = 0; i < jData.length(); i++) {
                        JSONObject c = jData.getJSONObject(i);

                        myList1.add(c.getString("nama_kategori"));
                        myList2.add(c.getString("kode_kategori"));
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            myAdapter1.notifyDataSetChanged();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpHandler sh = new HttpHandler();
            String url = "http://10.0.2.2/sipk/api/user_get_kategori/";
            String jsonStr = sh.makeServiceCall(url);

            return jsonStr;
        }
    }

    private class sendLaporan extends AsyncTask <String,Void,String>{

        @Override
        protected String doInBackground(String... params) {
            String reg_url = "http://10.0.2.2/sipk/api/user_lapor/";
            String text = "";

            Log.d("SIPK", reg_url);

            String id_pengguna = params[0];
            String judul_keluhan = params[1];
            String kode_kategori = params[2];
            String keluhan = params[3];
            String laporan_status = "3";

            try{
                URL url = new URL(reg_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();

                String data = URLEncoder.encode("id_pengguna","UTF-8") + "=" + URLEncoder.encode(id_pengguna, "UTF-8") + "&" +
                        URLEncoder.encode("judul_keluhan", "UTF-8") + "=" +  URLEncoder.encode(judul_keluhan,"UTF-8") + "&" +
                        URLEncoder.encode("kode_kategori", "UTF-8") + "=" +  URLEncoder.encode(kode_kategori,"UTF-8") + "&" +
                        URLEncoder.encode("laporan_status", "UTF-8") + "=" +  URLEncoder.encode(laporan_status,"UTF-8") + "&" +
                        URLEncoder.encode("keluhan", "UTF-8") + "=" +  URLEncoder.encode(keluhan,"UTF-8");

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