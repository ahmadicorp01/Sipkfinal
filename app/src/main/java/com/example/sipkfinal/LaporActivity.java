package com.example.sipkfinal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

public class LaporActivity extends AppCompatActivity {

    int id_user = 0;
    App app;
    String uploadedKeluhanImg;
    SharedPreferences sharedPreferences;
    TextInputLayout judul_keluhan,keluhan;
    Spinner spinner;
    Button btn_lapor_tambah,button2;
    ArrayList<String> myList1,myList2;
    ProgressDialog dialog;
    ImageView imglapor;
    private ArrayAdapter<String>myAdapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lapor);

        sharedPreferences = getSharedPreferences("SIPK", MODE_PRIVATE);
        id_user = sharedPreferences.getInt("id_user", 0);
        app = new App(this);
        judul_keluhan = findViewById(R.id.judul_keluhan);
        keluhan = findViewById(R.id.keluhan);

        imglapor = findViewById(R.id.imgKeluhan);

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Laporan Keluhan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                if (!validateJudulKeluhan()) {
                    return;
                }
                if (!validateKeluhan()) {
                    return;
                }
                String kategori = myList2.get(spinner.getSelectedItemPosition());
                Log.d("SIPK", kategori);
                new sendLaporan().execute(Integer.toString(id_user),
                        judul_keluhan.getEditText().getText().toString(),
                        kategori,
                        keluhan.getEditText().getText().toString());
                Toast.makeText(LaporActivity.this,"Laporan Berhasil Dibuat", Toast.LENGTH_SHORT).show();
                openDaftarActivity();
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
    private boolean validateJudulKeluhan() {
        String emailInput = judul_keluhan.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            judul_keluhan.setError("Wajib Diisi");
            return false;
        } else {
            judul_keluhan.setError(null);
            return true;
        }
    }

    private boolean validateKeluhan() {
        String emailInput = keluhan.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            keluhan.setError("Wajib Diisi");
            return false;
        } else {
            keluhan.setError(null);
            return true;
        }
    }

    public void browseImageLaporan(View v) {
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
                final String pathImgKeluhan = FilePath.getPath(this, imageUri);

                ImageView imgKeluhan = findViewById(R.id.imgKeluhan);
                imgKeluhan.setImageBitmap(selectedImage);
                imgKeluhan.setScaleType(ImageView.ScaleType.CENTER_CROP);

                dialog = ProgressDialog.show(this,"","Mengupload foto ...",true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        uploadFile(pathImgKeluhan);
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
            String laporan_status = "1";

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

            try{
                JSONObject obj = new JSONObject(result);
                Toast.makeText(LaporActivity.this, obj.getString("data"), Toast.LENGTH_SHORT).show();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private void tambahGambar(){
        dialog = ProgressDialog.show(this, "", "Menyimpan data", true);
                new sendLaporan().execute(uploadedKeluhanImg);
    }

    public void openDaftarActivity(){
        Intent intent = new Intent (this, DaftarActivity.class);
        startActivity(intent);
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
                    Snackbar.make(findViewById(R.id.layout_lapor_keluhan), "Sumber tidak ditemukan!", Snackbar.LENGTH_LONG).show();
                }
            });
            return 0;
        }
        else {
            try {
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(app.API("user_upload_img"));

                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("laporan_file", selectedFilePath);

                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);

                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"laporan_file\"; filename=\""
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
                if (serverResponseCode == 200) {
                    InputStream response = connection.getInputStream();
                    final String reply = convertStreamToString(response);

                    uploadedKeluhanImg = reply;


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Snackbar.make(findViewById(R.id.layout_lapor_keluhan), "Gambar berhasil diupload!", Snackbar.LENGTH_LONG).show();
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
}