//package com.example.sipkfinal;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.UnsupportedEncodingException;
//
//public class JSONParser {
//
//    static InputStream is = null;
//    static JSONObject jobj = null;
//    static String json = "";
//
//    public JSONParser(){
//
//    }
////
////    public JSONObject makeHttpRequest(String url){
////        DefaultHttpClient httpclient = new DefaultHttpClient();
////        HttpPost httppost = new HttpPost(url);
////
////        try {
////            HttpHandler httpresponse = httpclient.execute(httppost);
////            HttpEntity httpentity = httpresponse.getEntity();
////            is = httpentity.getContent();
////
////        } catch (ClientProtocolException e) {
////            // TODO Auto-generated catch block
////            e.printStackTrace();
////        } catch (IOException e) {
////            // TODO Auto-generated catch block
////            e.printStackTrace();
////        }
////
////        try {
////            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
////            StringBuilder sb = new StringBuilder();
////            String line = null;
////            try {
////                while((line = reader.readLine())!=null){
////                    sb.append(line+"\n");
////
////                }
////                is.close();
////                json = sb.toString();
////                try {
////                    jobj = new JSONObject(json);
////                } catch (JSONException e) {
////                    // TODO Auto-generated catch block
////                    e.printStackTrace();
////                }
////            } catch (IOException e) {
////                // TODO Auto-generated catch block
////                e.printStackTrace();
////            }
////
////        } catch (UnsupportedEncodingException e) {
////            // TODO Auto-generated catch block
////            e.printStackTrace();
////        }
////
////        return jobj;
////
////    }
////
////    public JSONObject makeHttpRequest(String url, String ApiKey){
////        DefaultHttpClient httpclient = new DefaultHttpClient();
////        HttpPost httppost = new HttpPost(url);
////        httppost.addHeader("Api_Key", ApiKey);
////
////        try {
////            HttpResponse httpresponse = httpclient.execute(httppost);
////            HttpEntity httpentity = httpresponse.getEntity();
////            is = httpentity.getContent();
////
////        } catch (ClientProtocolException e) {
////            // TODO Auto-generated catch block
////            e.printStackTrace();
////        } catch (IOException e) {
////            // TODO Auto-generated catch block
////            e.printStackTrace();
////        }
////
////        try {
////            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
////            StringBuilder sb = new StringBuilder();
////            String line = null;
////            try {
////                while((line = reader.readLine())!=null){
////                    sb.append(line+"\n");
////
////                }
////                is.close();
////                json = sb.toString();
////                try {
////                    jobj = new JSONObject(json);
////                } catch (JSONException e) {
////                    // TODO Auto-generated catch block
////                    e.printStackTrace();
////                }
////            } catch (IOException e) {
////                // TODO Auto-generated catch block
////                e.printStackTrace();
////            }
////
////        } catch (UnsupportedEncodingException e) {
////            // TODO Auto-generated catch block
////            e.printStackTrace();
////        }
////
////        return jobj;
////
////    }
//
//}