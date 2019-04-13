package com.example.sipkfinal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatAdapterHolder> {
    private ArrayList<ChatItem> mChatItem;
    private boolean kanan = false;

    public static class ChatAdapterHolder extends RecyclerView.ViewHolder{
        public TextView nama_pengguna,tanggapan,waktu;


        public ChatAdapterHolder(@NonNull final View itemView) {
            super(itemView);
            nama_pengguna = itemView.findViewById(R.id.cnama_pengguna);
            tanggapan = itemView.findViewById(R.id.cpesan);
            waktu = itemView.findViewById(R.id.cwaktu);

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

    }

    @Override
    public int getItemCount() {
        return mChatItem.size();
    }
}
