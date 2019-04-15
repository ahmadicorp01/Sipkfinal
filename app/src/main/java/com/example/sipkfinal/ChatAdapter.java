package com.example.sipkfinal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatAdapterHolder> {
    private ArrayList<ChatItem> mChatItem;
    private boolean kanan = false;
    boolean isImageFitToScreen;
    private ImageView imagechat;

    public static class ChatAdapterHolder extends RecyclerView.ViewHolder{
        private TextView nama_pengguna,tanggapan,waktu;
        private ImageView imagechat;

        public ChatAdapterHolder(@NonNull final View itemView) {
            super(itemView);
            nama_pengguna = itemView.findViewById(R.id.cnama_pengguna);
            tanggapan = itemView.findViewById(R.id.cpesan);
            waktu = itemView.findViewById(R.id.cwaktu);
            imagechat = itemView.findViewById(R.id.img_chat);

        }
    }

    public ChatAdapter(ArrayList<ChatItem> chatItems){
        mChatItem = chatItems;
    }
    @NonNull
    @Override
    public ChatAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_pengguna_row, parent, false);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) v.getLayoutParams();

        if (kanan) {
            v.setBackgroundResource(R.drawable.layoutsender);
            layoutParams.setMargins(150, 6, 6, 6);
            v.requestLayout();
        }
        else {
            v.setBackgroundResource(R.drawable.layoutreceiver);
            layoutParams.setMargins(6, 6, 150, 6);
            v.requestLayout();
        }

        ChatAdapterHolder cah = new ChatAdapterHolder(v);
        return cah;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatAdapterHolder holder, int i) {
        ChatItem currentItem = mChatItem.get(i);
        kanan = currentItem.getKanan();

        holder.nama_pengguna.setText(currentItem.getNama());
        holder.tanggapan.setText(currentItem.getTanggapan());
        holder.waktu.setText(currentItem.getWaktu());

        holder.imagechat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH ){
                    imagechat.setSystemUiVisibility( View.SYSTEM_UI_FLAG_HIDE_NAVIGATION );

                }
                else if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB )
                    imagechat.setSystemUiVisibility( View.STATUS_BAR_HIDDEN );
                else{}

            }
        });

        Log.d("SIPK",currentItem.getImage());
        if (!currentItem.getImage().isEmpty()) {
            Context ctx = holder.imagechat.getContext();
            String url = ctx.getString(R.string.ASSETS_URL) + currentItem.getImage();

            Picasso.with(ctx).invalidate(url);
            Picasso.with(ctx).load(url).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.imagechat);
            holder.imagechat.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }else {
            holder.imagechat.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mChatItem.size();
    }
}
