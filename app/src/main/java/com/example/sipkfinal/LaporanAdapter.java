package com.example.sipkfinal;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class LaporanAdapter extends RecyclerView.Adapter<LaporanAdapter.LaporanAdapterHolder> {
    private ArrayList<LaporanItem> mLaporanItem;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
     void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class LaporanAdapterHolder extends RecyclerView.ViewHolder{
        public TextView judul_keluhan,nama_kategori,waktu;

        public LaporanAdapterHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            judul_keluhan = itemView.findViewById(R.id.mjudul_keluhan);
            nama_kategori = itemView.findViewById(R.id.mnama_kategori);
            waktu = itemView.findViewById(R.id.mwaktu);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener !=null){
                        int position = getAdapterPosition();
                        if(position !=RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public LaporanAdapter(ArrayList<LaporanItem> laporanItems){
        mLaporanItem = laporanItems;
    }
    @NonNull
    @Override
    public LaporanAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.daftar_laporan_row, parent, false);
        LaporanAdapterHolder lah = new LaporanAdapterHolder(v, mListener);
        return lah;
    }

    @Override
    public void onBindViewHolder(@NonNull LaporanAdapterHolder holder, int i) {
        LaporanItem currentItem = mLaporanItem.get(i);

        holder.judul_keluhan.setText(currentItem.getJudul_keluhan());
        holder.nama_kategori.setText(currentItem.getNama_kategori());
        holder.waktu.setText(currentItem.getWaktu());
    }

    @Override
    public int getItemCount() {
        return mLaporanItem.size();
    }
}
