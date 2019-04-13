package com.example.sipkfinal;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class LaporanAdapter extends RecyclerView.Adapter<LaporanAdapter.LaporanAdapterHolder> {
    private ArrayList<LaporanItem> mLaporanItem;

    public static class LaporanAdapterHolder extends RecyclerView.ViewHolder{
        public TextView id_laporan,judul_keluhan,nama_kategori,waktu;

        public LaporanAdapterHolder(@NonNull final View itemView) {
            super(itemView);
            id_laporan = itemView.findViewById(R.id.mid_laporan);
            judul_keluhan = itemView.findViewById(R.id.mjudul_keluhan);
            nama_kategori = itemView.findViewById(R.id.mnama_kategori);
            waktu = itemView.findViewById(R.id.mwaktu);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context c = itemView.getRootView().getContext();


                    Intent intent = new Intent(c, TanggapanActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    intent.putExtra("id_laporan", id_laporan.getText());
                    c.startActivity(intent);
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
        LaporanAdapterHolder lah = new LaporanAdapterHolder(v);
        return lah;
    }

    @Override
    public void onBindViewHolder(@NonNull LaporanAdapterHolder holder, int i) {
        LaporanItem currentItem = mLaporanItem.get(i);

        holder.id_laporan.setText(Integer.toString(currentItem.getMid_laporan()));
        holder.judul_keluhan.setText(currentItem.getJudul_keluhan());
        holder.nama_kategori.setText(currentItem.getNama_kategori());
        holder.waktu.setText(currentItem.getWaktu());
    }

    @Override
    public int getItemCount() {
        return mLaporanItem.size();
    }
}
