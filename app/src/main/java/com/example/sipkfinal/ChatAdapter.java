package com.example.sipkfinal;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatAdapterHolder> {
    private ArrayList<ChatItem> mChatItem;

    public static class ChatAdapterHolder extends RecyclerView.ViewHolder{
        public TextView nama,tanggapan,waktu;

        public ChatAdapterHolder(@NonNull final View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.cnama_pengguna);
            tanggapan = itemView.findViewById(R.id.cpesan);
            waktu = itemView.findViewById(R.id.cwaktu);

        }
    }
    public ChatAdapter(ArrayList<ChatItem> chatItems){
        mChatItem = chatItems;
    }
    @NonNull
    @Override
    public ChatAdapter.ChatAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_pengguna_row, parent, false);
        ChatAdapter.ChatAdapterHolder cah = new ChatAdapter.ChatAdapterHolder(v);
        return cah;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatAdapterHolder holder, int i) {
        ChatItem currentItem = mChatItem.get(i);

        holder.nama.setText(currentItem.getNama());
        holder.tanggapan.setText(currentItem.getTanggapan());
        holder.waktu.setText(currentItem.getWaktu());
    }

    @Override
    public int getItemCount() {
        return mChatItem.size();
    }
}
