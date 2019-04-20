package com.example.sipkfinal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
    private int jumlahtanggapan = 0;
    App app;
    String uploadedChatImg;
    ProgressDialog dialog;
    EditText tanggapan;
    TextView text_keluhan, text_status;
    CardView cardview_status;
    Button kirim, arrow_back, browsegambar;
    int id_user = 0;
    int id_pengguna = 0;
    SharedPreferences sharedPreferences;
    private ArrayList<ChatItem> chatItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tanggapan);

        sharedPreferences = getSharedPreferences("SIPK", MODE_PRIVATE);
        id_user = sharedPreferences.getInt("id_user", 0);
        id_pengguna = sharedPreferences.getInt("id_pengguna", 0);

        browsegambar = findViewById(R.id.btn_img_chat);
        text_keluhan = findViewById(R.id.text_keluhan);
        text_keluhan.setMovementMethod(new ScrollingMovementMethod());

        text_status = findViewById(R.id.text_status);
        cardview_status = findViewById(R.id.card_viewstatus);

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
                        tanggapan.getText().toString(),
                        uploadedChatImg);
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

                        chatItems.add(new ChatItem(
                                c.getString("nama_pengguna"),
                                c.getString("tanggapan"),
                                c.getString("waktu"),
                                (id_pengguna == c.getInt("id_pengguna") ? true : false),
                                c.getString("gambar")));


//                        Log.d("SIPK", String.valueOf(c.getInt("id_pengguna")));
                        cAdapter.notifyDataSetChanged();

                        jumlahtanggapan++;
                    }

//                    if (jumlahtanggapan <1 ){
//                        tanggapan.setVisibility(View.INVISIBLE);
//                        kirim.setVisibility(View.INVISIBLE);
//                        browsegambar.setVisibility(View.INVISIBLE);
//                    }else {
//                        tanggapan.setVisibility(View.VISIBLE);
//                        kirim.setVisibility(View.VISIBLE);
//                        browsegambar.setVisibility(View.VISIBLE);
//                    }
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
    public void browseImageChat(View v) {
        String imageUri = "";

        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        Intent chooser = Intent.createChooser(galleryIntent, "Complete action using");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { cameraIntent });
        startActivityForResult(chooser, 909);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 909) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapSupport.handleSamplingAndRotationBitmap(this, imageUri); //BitmapFactory.decodeStream(imageStream);
                final String pathImgChat = FilePath.getPath(this, imageUri);

                ImageView imgChat = findViewById(R.id.img_chat);
                imgChat.setImageBitmap(selectedImage);
                imgChat.setScaleType(ImageView.ScaleType.CENTER_CROP);

                dialog = ProgressDialog.show(this,"","Mengupload foto ...",true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        uploadFile(pathImgChat);
                    }
                }).start();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Terjadi kesalahan!", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Terjadi kesalahan!", Toast.LENGTH_LONG).show();
            }

        }
    }

    private int uploadFile(final String selectedFilePath){
        int serverResponseCode = 0;

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";


        int bytesRead,bytesAvailable,bufferSize;
        byte[] buffer;
        int maxBufferSize = 10 * 1024 * 1024;
        File selectedFile = new File(selectedFilePath);


        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length - 1];

        if (!selectedFile.isFile()) {
            dialog.dismiss();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Snackbar.make(findViewById(R.id.layout_tanggapan), "Sumber tidak ditemukan!", Snackbar.LENGTH_LONG).show();
                }
            });
            return 0;
        }
        else {
            try {
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(app.API("user_upload_img_chat"));

                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("gambar", selectedFilePath);

                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"gambar\"; filename=\""
                        + selectedFilePath + "\"" + lineEnd);

                dataOutputStream.writeBytes(lineEnd);

                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 10 MB
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer,0,bufferSize);

                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0){
                    //write the bytes read from inputstream
                    dataOutputStream.write(buffer,0,bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable,maxBufferSize);
                    bytesRead = fileInputStream.read(buffer,0,bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = connection.getResponseCode();
                String serverResponseMessage = connection.getResponseMessage();
                //response code of 200 indicates the server status OK
                Log.d("SIPK", String.valueOf(serverResponseCode));
                if (serverResponseCode == 200) {
                    InputStream response = connection.getInputStream();
                    final String reply = convertStreamToString(response);
                    uploadedChatImg = reply;
                    Log.d("SIPK",reply);


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Snackbar.make(findViewById(R.id.layout_tanggapan), "Gambar berhasil ditambahkan!", Snackbar.LENGTH_LONG).show();
                        }
                    });
                }
                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(TanggapanActivity.this, "Kepanggil Ngga", Toast.LENGTH_SHORT).show();
//                        Snackbar.make(findViewById(R.id.layout_tambah_lowongan),"Berkas tidak ditemukan!", Snackbar.LENGTH_LONG).show();
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }

            dialog.dismiss();
            return serverResponseCode;
        }
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
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
                        cardview_status.setBackgroundResource(R.color.brownstatus);

                        tanggapan.setVisibility(View.VISIBLE);
                        kirim.setVisibility(View.VISIBLE);
                        browsegambar.setVisibility(View.VISIBLE);
                    }if (c.getInt("laporan_status") == 2){
                        text_status.setText("Proses");
                        cardview_status.setBackgroundResource(R.color.bluetstatus);

                        tanggapan.setVisibility(View.VISIBLE);
                        kirim.setVisibility(View.VISIBLE);
                        browsegambar.setVisibility(View.VISIBLE);
                    }if(c.getInt("laporan_status")==3){
                        cardview_status.setBackgroundResource(R.color.greenstatus);
                        text_status.setText("Selesai");

                        tanggapan.setVisibility(View.INVISIBLE);
                        kirim.setVisibility(View.INVISIBLE);
                        browsegambar.setVisibility(View.INVISIBLE);
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
            String gambar = "0";
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
                                URLEncoder.encode("gambar", "UTF-8") + "=" +  URLEncoder.encode(gambar,"UTF-8") + "&" +
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
