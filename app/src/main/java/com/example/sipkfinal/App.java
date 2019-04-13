package com.example.sipkfinal;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

public class App {

    private static Context ctx;
    private static String CHANNEL_ID = "SIPK_NOTIFICATION_CHANNEL";


    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = ctx.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public App(Context ctx) {
        this.ctx = ctx;
        createNotificationChannel();
    }

    private String getString(int code){
        return ctx.getResources().getString(code);
    }

    public static String API(String URI) {
        return ctx.getString(R.string.API_URL) + URI;
    }

    private static boolean isOnline(){
        ConnectivityManager connectivityManager = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo !=null && networkInfo.isAvailable() && networkInfo.isConnected();
    }
    private static boolean isConnectedWifi() {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    private static boolean isConnectedMobile() {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;
    }

    public static boolean cekInternet(View v) {
        if (isOnline()) {
            if (isConnectedMobile() || isConnectedWifi()) {
                return true;
            }
        }
        else {
            Snackbar.make(v, "Tidak ada koneksi Intetnet!", Snackbar.LENGTH_LONG).show();
        }

        return false;
    }

    public static boolean cekInternet() {
        if (isOnline()) {
            if (isConnectedMobile() || isConnectedWifi()) {
                return true;
            }
        }

        return false;
    }

    public static void showNotification(String sTitle, String sContent) {
        Intent intent = new Intent(ctx, SplashScreen.class);
        intent.putExtra("notif", true);

        int requestID = (int) System.currentTimeMillis();
        int flags = PendingIntent.FLAG_CANCEL_CURRENT;

        PendingIntent pIntent = PendingIntent.getActivity(ctx, requestID, intent, flags);

        Notification notif = new NotificationCompat.Builder(ctx, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(ContextCompat.getColor(ctx, R.color.backgroundcolor))
                .setContentTitle(sTitle)
                .setContentText(sContent)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(sContent))
                .build();

        NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, notif);
    }

    public static void showNotification(String sTitle, String sContent, boolean version) {
        final String appPackageName = ctx.getPackageName(); // getPackageName() from Context or Activity object
        Intent intent;

        try {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName));
        }
        catch (android.content.ActivityNotFoundException e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName));
        }

        int requestID = (int) System.currentTimeMillis();
        int flags = PendingIntent.FLAG_CANCEL_CURRENT;

        PendingIntent pIntent = PendingIntent.getActivity(ctx, requestID, intent, flags);
        Notification noti = new NotificationCompat.Builder(ctx, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(ContextCompat.getColor(ctx, R.color.backgroundcolor))
                .setContentTitle(sTitle)
                .setContentText(sContent)
                .setContentIntent(pIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(sContent))
                .setAutoCancel(true)
                .build();

        NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, noti);
    }
}
